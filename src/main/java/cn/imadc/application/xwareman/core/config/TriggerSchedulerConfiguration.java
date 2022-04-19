package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.data.component.error.ThreadPoolTaskSchedulerErrorHandler;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.property.AppProp;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * <p>
 * 触发器定时器配置
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-19
 */
@AllArgsConstructor
@Configuration
public class TriggerSchedulerConfiguration {

    private final AppProp appProp;
    private final ThreadPoolTaskSchedulerErrorHandler threadPoolTaskSchedulerErrorHandler;

    @Bean(value = Constant.THREAD_POOL_TASK_SCHEDULER)
    public ThreadPoolTaskScheduler triggerThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(appProp.getInstanceItemTriggerTaskCorePoolSize());
        threadPoolTaskScheduler.setThreadNamePrefix(Constant.INSTANCE_ITEM_TRIGGER_TASK);
        threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskScheduler.setAwaitTerminationMillis(30 * 1000);
        threadPoolTaskScheduler.afterPropertiesSet();
        threadPoolTaskScheduler.setErrorHandler(threadPoolTaskSchedulerErrorHandler);
        return threadPoolTaskScheduler;
    }

}
