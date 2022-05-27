package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.container.TriggerContainerRegister;
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
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 触发器容器配置
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

    private final TriggerContainerRegister triggerContainerRegister;

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        triggerContainerRegister.setApplicationContext((ConfigurableApplicationContext) applicationContext);
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

            triggerContainerRegister.registerContainer(beanName, bean, trigger);
        }
    }
}
