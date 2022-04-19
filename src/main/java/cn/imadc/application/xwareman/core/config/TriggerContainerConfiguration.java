package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.container.DefaultTriggerContainer;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.trigger.dto.request.TriggerFindReqDTO;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import cn.imadc.application.xwareman.module.trigger.service.ITriggerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-19
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class TriggerContainerConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

    private final ITriggerService triggerService;

    @Qualifier(value = Constant.THREAD_POOL_TASK_SCHEDULER)
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {

        TriggerFindReqDTO triggerFindReqDTO = new TriggerFindReqDTO();
        List<Trigger> triggerList = triggerService.listTrigger(triggerFindReqDTO);

        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(StrategyTrigger.class)
                .entrySet().stream().filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        beans.forEach((k, v) -> assignTrigger(k, v, triggerList));
    }

    private void assignTrigger(String beanName, Object bean, List<Trigger> triggerList) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);
        StrategyTrigger annotation = clazz.getAnnotation(StrategyTrigger.class);

        for (Trigger trigger : triggerList) {
            if (trigger.getStrategy() != annotation.strategy().getValue()) continue;

            registerContainer(beanName, bean, trigger);
        }
    }

    private void registerContainer(String beanName, Object bean, Trigger trigger) {
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
        threadPoolTaskScheduler.scheduleAtFixedRate(container, 1000);
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
}
