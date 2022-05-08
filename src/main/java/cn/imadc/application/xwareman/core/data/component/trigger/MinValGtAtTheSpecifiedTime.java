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
 * 指定时间内最小值超出限额
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@StrategyTrigger(strategy = TriggerStrategyEnum.MIN_VAL_GT_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class MinValGtAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;

    @Override
    public void handle(Trigger trigger) {
        List<Object> colDataList = instanceItemDataComponent.selectColAtSpecifiedTime(trigger);

        if (CollectionUtils.isEmpty(colDataList)) return;

        Object min = colDataList.stream().min((o1, o2) -> {
            double d1 = Double.parseDouble(o1.toString());
            double d2 = Double.parseDouble(o2.toString());
            return d1 == d2 ? 0 : d1 - d2 > 0 ? 1 : -1;
        }).orElse(null);

        if (null == min) return;

        Double threshold = trigger.getThreshold();
        Double minV = Double.parseDouble(min.toString());

        System.out.println(threshold > minV);
    }
}
