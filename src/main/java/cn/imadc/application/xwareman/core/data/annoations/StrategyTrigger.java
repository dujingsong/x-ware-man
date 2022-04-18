package cn.imadc.application.xwareman.core.data.annoations;

import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 触发器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface StrategyTrigger {

    /**
     * 策略类型
     *
     * @return 见枚举TriggerStrategyEnum
     */
    TriggerStrategyEnum strategy();
}
