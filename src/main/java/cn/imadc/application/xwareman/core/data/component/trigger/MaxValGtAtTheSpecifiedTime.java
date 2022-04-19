package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 指定时间内最大值超出限额
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@StrategyTrigger(strategy = TriggerStrategyEnum.MAX_VAL_GT_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class MaxValGtAtTheSpecifiedTime implements ITriggerStrategy {

    @Override
    public void handle(Trigger trigger) {

    }
}
