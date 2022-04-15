package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.data.property.AppProp;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@Configuration
@EnableScheduling // 开启定时任务
public class ScheduledConfiguration implements SchedulingConfigurer {

    private final AppProp appProp;

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("SCHEDULED_" + counter.incrementAndGet());// 设置定时任务线程名称的前缀
        int corePoolSize = appProp.getScheduledCorePoolSize(); // 设置定时任务的核心线程数
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(corePoolSize, executor));
    }
}