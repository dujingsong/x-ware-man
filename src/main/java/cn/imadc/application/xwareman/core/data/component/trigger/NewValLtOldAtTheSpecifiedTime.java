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

import java.util.List;

/**
 * <p>
 * 指定时间内新值小于旧值
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@StrategyTrigger(strategy = TriggerStrategyEnum.NEW_VAL_LT_OLD_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class NewValLtOldAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;
    private final InstanceEventStoreComponent instanceEventStoreComponent;

    @Override
    public void handle(Trigger trigger) {
        try {
            // 查出指定字段的数据
            List<Object> colDataList = instanceItemDataComponent.selectColAtSpecifiedTime(trigger);

            if (CollectionUtils.isEmpty(colDataList)) return;

            if (colDataList.size() == 1) return;

            // 取出老值和新值
            Object oldest = colDataList.get(0);
            Object newest = colDataList.get(colDataList.size() - 1);

            double oldestV = Double.parseDouble(oldest.toString());
            double newestV = Double.parseDouble(newest.toString());

            // 新老值比较
            if (newestV >= oldestV) return;

            String eventDesc = trigger.getCol() + "新值小于旧值";

            instanceEventStoreComponent.store(trigger, eventDesc);
        } catch (Exception e) {
            log.error("指定时间内新值小于旧值", e);
        }
    }
}
