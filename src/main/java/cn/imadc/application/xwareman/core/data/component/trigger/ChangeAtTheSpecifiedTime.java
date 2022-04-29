package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.component.item.InstanceItemDataComponent;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 指定时间内发生变化
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@StrategyTrigger(strategy = TriggerStrategyEnum.CHANGE_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class ChangeAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;

    @Override
    public void handle(Trigger trigger) {
        List<Object> colDataList = instanceItemDataComponent.selectColAtSpecifiedTime(trigger);
        if (CollectionUtils.isEmpty(colDataList)) return;

        long size = colDataList.stream().distinct().count();

        System.out.println(colDataList.size() == size);
    }
}
