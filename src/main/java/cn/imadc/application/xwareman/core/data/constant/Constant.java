package cn.imadc.application.xwareman.core.data.constant;

import cn.imadc.application.base.common.constant.BaseConstant;

/**
 * <p>
 * 系统常量
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
public final class Constant extends BaseConstant {

    // -----------------------------------------线程相关-----------------------------------------

    /**
     * 触发器定时器名称
     */
    public static final String THREAD_POOL_TASK_SCHEDULER = "triggerThreadPoolTaskScheduler";
    /**
     * 触发器线程名称前缀
     */
    public static final String INSTANCE_ITEM_TRIGGER_TASK = "INSTANCE-ITEM-TRIGGER-TASK-";
    /**
     * 定时任务线程前缀
     */
    public static final String SCHEDULED_THREAD_NAME_PREFIX = "SCHEDULED-TASK-";

    // -----------------------------------------其他-----------------------------------------
    /**
     * 日志分隔符
     */
    public static final String LOG_LINE_SEPARATOR
            = "--------------------------------------------------------------------------------------------------------";

}
