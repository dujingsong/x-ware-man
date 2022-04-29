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
 * 指定时间内新值小于旧值
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@StrategyTrigger(strategy = TriggerStrategyEnum.NEW_VAL_LT_OLD_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class NewValLtOldAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;

    @Override
    public void handle(Trigger trigger) {
        List<Object> colDataList = instanceItemDataComponent.selectColAtSpecifiedTime(trigger);

        if (CollectionUtils.isEmpty(colDataList)) return;

        if (colDataList.size() == 1) return;

        Object oldest = colDataList.get(0);
        Object newest = colDataList.get(colDataList.size() - 1);

        double oldestV = Double.parseDouble(oldest.toString());
        double newestV = Double.parseDouble(newest.toString());

        System.out.println(newestV > oldestV);
    }
}
