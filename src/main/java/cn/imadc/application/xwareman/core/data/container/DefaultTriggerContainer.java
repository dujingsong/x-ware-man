package cn.imadc.application.xwareman.core.data.container;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.core.data.strategy.TriggerContainer;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

/**
 * <p>
 * 默认的监控项目触发器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-11
 */
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
@SuppressWarnings("WeakerAccess")
public class DefaultTriggerContainer implements InitializingBean,
        TriggerContainer, SmartLifecycle, ApplicationContextAware, Runnable {

    private ApplicationContext applicationContext;
    private boolean running;
    private StrategyTrigger annotation;
    private String name;
    private Trigger trigger;
    private ITriggerStrategy bean;

    @Override
    public void destroy() throws Exception {
        this.setRunning(false);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public void run() {
        this.bean.handle(this.trigger);
    }
}
