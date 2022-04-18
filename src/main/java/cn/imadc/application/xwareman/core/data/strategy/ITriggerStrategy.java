package cn.imadc.application.xwareman.core.data.strategy;

import cn.imadc.application.xwareman.module.trigger.entity.Trigger;

/**
 * <p>
 * 触发器策略
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
public interface ITriggerStrategy {

    /**
     * 处理
     *
     * @param trigger 触发器
     */
    void handle(Trigger trigger);

}
