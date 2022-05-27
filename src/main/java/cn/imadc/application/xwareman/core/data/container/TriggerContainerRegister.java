package cn.imadc.application.xwareman.core.data.container;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 监控项目触发器容器注册器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-25
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TriggerContainerRegister {

    @Qualifier(value = Constant.THREAD_POOL_TASK_SCHEDULER)
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ConfigurableApplicationContext applicationContext;
    private final Map<Long, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    public void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void registerContainer(Trigger trigger) {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(StrategyTrigger.class)
                .entrySet().stream().filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (Map.Entry<String, Object> entry : beans.entrySet()) {

            Class<?> clazz = AopProxyUtils.ultimateTargetClass(entry.getValue());
            StrategyTrigger annotation = clazz.getAnnotation(StrategyTrigger.class);

            if (trigger.getStrategy() != annotation.strategy().getValue()) continue;

            registerContainer(entry.getKey(), entry.getValue(), trigger);
        }
    }

    public void registerContainer(String beanName, Object bean, Trigger trigger) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (!ITriggerStrategy.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + ITriggerStrategy.class.getName());
        }

        StrategyTrigger annotation = clazz.getAnnotation(StrategyTrigger.class);

        String containerBeanName = String.format("%s_%s", DefaultTriggerContainer.class.getName(), trigger.getId());
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;

        genericApplicationContext.registerBean(containerBeanName, DefaultTriggerContainer.class,
                () -> createTriggerContainer(containerBeanName, (ITriggerStrategy) bean, annotation, trigger));

        DefaultTriggerContainer container = genericApplicationContext.getBean(containerBeanName,
                DefaultTriggerContainer.class);

        Integer periodType = trigger.getPeriodType();
        String period = trigger.getPeriod();

        if (periodType == 0) {
            long periodVal = Long.parseLong(period);
            periodVal = TimeUnit.MILLISECONDS.convert(periodVal, TimeUnit.MINUTES);
            ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(container, periodVal);
            scheduledFutureMap.put(trigger.getId(), scheduledFuture);
        } else {
            ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(container, new CronTrigger(period));
            scheduledFutureMap.put(trigger.getId(), scheduledFuture);
        }
    }

    private DefaultTriggerContainer createTriggerContainer(String containerBeanName, ITriggerStrategy bean,
                                                           StrategyTrigger annotation, Trigger trigger) {
        DefaultTriggerContainer container = new DefaultTriggerContainer();
        container.setAnnotation(annotation);
        container.setName(containerBeanName);
        container.setTrigger(trigger);
        container.setBean(bean);

        return container;
    }

    public boolean stopTrigger(Long triggerId) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(triggerId);
        if (null == scheduledFuture) return false;

        if (scheduledFuture.isCancelled()) return true;

        boolean cancelResult = scheduledFuture.cancel(true);
        if (cancelResult) scheduledFutureMap.remove(triggerId);

        return cancelResult;
    }

    public boolean removeContainer(Long triggerId) {
        String containerBeanName = String.format("%s_%s", DefaultTriggerContainer.class.getName(), triggerId);
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;

        try {
            Object bean = genericApplicationContext.getBean(containerBeanName);
        } catch (BeansException beansException) {
            log.warn("could not found bean with name {}", containerBeanName);
            return true;
        }

        genericApplicationContext.removeBeanDefinition(containerBeanName);

        return true;
    }

    public boolean checkCron(String cron) {
        try {
            new CronTrigger(cron);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
