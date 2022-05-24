package cn.imadc.application.xwareman.module.trigger.dto.data;

import cn.imadc.application.base.common.data.BaseData;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 触发器策略数据
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-24
 */
@Getter
@Setter
public class TriggerStrategyData extends BaseData {

    /**
     * -1 通用； 0 redis； 1 rocketmq
     */
    private int scope;

    /**
     * 描述
     */
    private String desc;

    /**
     * 策略枚举
     */
    private TriggerStrategyEnum strategy;

}
