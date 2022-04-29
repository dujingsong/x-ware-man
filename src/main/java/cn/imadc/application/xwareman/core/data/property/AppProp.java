package cn.imadc.application.xwareman.core.data.property;

import cn.imadc.application.xwareman.core.data.jdbc.sql.JdbcEventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 配置信息
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProp {

    // -----------------------------------------------会话相关-----------------------------------------------
    /**
     * 会话超时时间
     */
    private Long ctxTimeout;
    /**
     * 会话超时单位
     */
    private TimeUnit ctxTimeoutUnit;

    // -----------------------------------------------文件相关-----------------------------------------------
    /**
     * 文件存放路径
     */
    private String fileStorePath;
    /**
     * 头像文件存放路径
     */
    private String avatarPath;

    // -----------------------------------------------任务相关-----------------------------------------------
    /**
     * 定时任务核心线程数
     */
    private Integer scheduledCorePoolSize;

    /**
     * 监控项触发器任务核心线程数
     */
    private Integer instanceItemTriggerTaskCorePoolSize;

    // -----------------------------------------------log相关-----------------------------------------------
    /**
     * 日志配置
     */
    private Log log;

    @Getter
    @Setter
    public static class Log {
        // 打印异常
        private boolean printException;
        // 打印sql
        private boolean printJdbcEvent;
        // 打印会话日志体
        private boolean printContextLog;
        // 不需要打印的sql类型
        private List<JdbcEventType> excludeJdbcEventType;
        // 是否记录慢sql
        private boolean slowQueryLog;
        // 慢sql阈值（毫秒）
        private long longQueryTime;
    }
}
