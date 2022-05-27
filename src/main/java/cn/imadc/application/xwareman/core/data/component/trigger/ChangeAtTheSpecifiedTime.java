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
 * 指定时间内发生变化
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@StrategyTrigger(strategy = TriggerStrategyEnum.CHANGE_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class ChangeAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceItemDataComponent instanceItemDataComponent;
    private final InstanceEventStoreComponent instanceEventStoreComponent;

    @Override
    public void handle(Trigger trigger) {
        try {
            // 查询指定字段数据
            List<Object> colDataList = instanceItemDataComponent.selectColAtSpecifiedTime(trigger);
            if (CollectionUtils.isEmpty(colDataList)) return;

            // 是否存在不一样的数据
            boolean allEq = true;
            for (int i = 0; i < colDataList.size(); i++) {
                if (i == 0) continue;

                if (colDataList.get(i).equals(colDataList.get(i - 1))) {
                    continue;
                }

                allEq = false;
                break;
            }

            if (allEq) return;

            String eventDesc = trigger.getCol() + "发生变化";

            instanceEventStoreComponent.store(trigger, eventDesc);
        } catch (Exception e) {
            log.error("指定时间内发生变化", e);
        }
    }
}
