package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

    private final IItemRedisService itemRedisService;

    @Override
    public void handle(Trigger trigger) {
        String col = trigger.getCol();
        LocalDateTime begin = LocalDateTime.now();
        LocalDateTime end = begin.minusMinutes(10);
        List<Object> colDataList = itemRedisService.selectColAtSpecifiedTime(col, end, begin);
        System.out.printf("%s-%s\n", Thread.currentThread().getName(), trigger.getId());
    }
}
