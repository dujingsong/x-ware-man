package cn.imadc.application.xwareman.core.data.enums;

import cn.imadc.application.base.common.action.IEnumAble;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 触发器策略
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Getter
@AllArgsConstructor
public enum TriggerStrategyEnum implements IEnumAble {

    // 通用
    CHANGE_AT_THE_SPECIFIED_TIME(0, "指定时间内发生变化", -1, -1),
    NEW_VAL_LT_OLD_AT_THE_SPECIFIED_TIME(1, "指定时间内新值小于旧值", -1, 0),
    MAX_VAL_GT_AT_THE_SPECIFIED_TIME(2, "指定时间内最大值超出限额", -1, 0),
    AVG_VAL_GT_AT_THE_SPECIFIED_TIME(3, "指定时间内均值超出限额", -1, 0),
    MIN_VAL_GT_AT_THE_SPECIFIED_TIME(4, "指定时间内最小值超出限额", -1, 0),
    AVG_VAL_LT_AT_THE_SPECIFIED_TIME(3, "指定时间内均值低于限额", -1, 0),

    // redis
    PATTERN_KEY_GT_AT_THE_SPECIFIED_TIME(101, "指定时间内匹配的key超出限额", 0, -1),
    PATTERN_KEY_TTL_GT_AT_THE_SPECIFIED_TIME(102, "指定时间内匹配的key存活时间超出限额", 0, -1),

    // rocketmq

    ;
    private final int value;
    private final String description;
    // -1 通用； 0 redis； 1 rocketmq
    private final int scope;
    // -1 通用； 0 number
    private final int dataType;

    @Override
    public String v() {
        return this.toString();
    }

    public static TriggerStrategyEnum parse(int value) {
        for (TriggerStrategyEnum strategyEnum : TriggerStrategyEnum.values()) {
            if (strategyEnum.getValue() == value) return strategyEnum;
        }
        return null;
    }
}
