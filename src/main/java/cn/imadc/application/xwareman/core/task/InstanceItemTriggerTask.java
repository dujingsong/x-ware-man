package cn.imadc.application.xwareman.core.task;

import cn.imadc.application.xwareman.core.component.TriggerStrategyContext;
import cn.imadc.application.xwareman.module.trigger.dto.request.TriggerFindReqDTO;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import cn.imadc.application.xwareman.module.trigger.service.ITriggerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 监控项目触发器定时任务
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@AllArgsConstructor
@Component
public class InstanceItemTriggerTask {

    private final ITriggerService triggerService;
    private final TriggerStrategyContext triggerStrategyContext;

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 1000)
    public void collectRedisInstanceItem() {
        TriggerFindReqDTO triggerFindReqDTO = new TriggerFindReqDTO();
        List<Trigger> triggerList = triggerService.listTrigger(triggerFindReqDTO);
        triggerList.forEach(triggerStrategyContext::handle);
    }
}
