package cn.imadc.application.xwareman.core.data.component.error;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * <p>
 * 定时任务异常处理器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-19
 */
@Slf4j
@AllArgsConstructor
@Component
public class ThreadPoolTaskSchedulerErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("ThreadPoolTaskSchedulerErrorHandler", t);
    }
}
