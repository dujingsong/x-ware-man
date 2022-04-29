package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.component.item.InstanceItemDataComponent;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 指定时间内符合条件的数据数量大于
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@StrategyTrigger(strategy = TriggerStrategyEnum.PATTERN_KEY_GT_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class PatternKeyGtAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;

    @Override
    public void handle(Trigger trigger) {

    }
}
