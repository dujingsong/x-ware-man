package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.component.item.InstanceEventStoreComponent;
import cn.imadc.application.xwareman.core.data.component.item.InstanceItemDataComponent;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * <p>
 * 指定时间内均值超出限额
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@StrategyTrigger(strategy = TriggerStrategyEnum.AVG_VAL_GT_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class AvgValGtAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;
    private final InstanceEventStoreComponent instanceEventStoreComponent;

    @Override
    public void handle(Trigger trigger) {
        try {
            // 查出指定字段的数据
            List<Object> colDataList = instanceItemDataComponent.selectColAtSpecifiedTime(trigger);
            if (CollectionUtils.isEmpty(colDataList)) return;

            // 求和
            BigDecimal total = new BigDecimal(0);

            for (Object colData : colDataList) {
                BigDecimal colDataBigDecimal = BigDecimal.valueOf(Double.parseDouble(colData.toString()));
                total = total.add(colDataBigDecimal);
            }

            // 平均数
            BigDecimal avg = total.divide(new BigDecimal(colDataList.size()), 10, RoundingMode.FLOOR);

            // 阈值
            BigDecimal threshold = BigDecimal.valueOf(trigger.getThreshold());

            // 与阈值比较
            int compareToResult = avg.compareTo(threshold);

            if (compareToResult <= 0) return;

            String eventDesc = trigger.getCol() + "均值超出限额";

            instanceEventStoreComponent.store(trigger, eventDesc);
        } catch (Exception e) {
            log.error("指定时间内均值超出限额", e);
        }
    }
}
