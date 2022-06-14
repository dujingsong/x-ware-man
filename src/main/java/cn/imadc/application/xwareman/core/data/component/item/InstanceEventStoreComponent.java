package cn.imadc.application.xwareman.core.data.component.item;

import cn.imadc.application.xwareman.module.event.entity.ItemEvent;
import cn.imadc.application.xwareman.module.event.service.IItemEventService;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRocketmqService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 触发事件存储处理器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-26
 */
@Slf4j
@AllArgsConstructor
@Component
public class InstanceEventStoreComponent {

    private final IItemEventService itemEventService;
    private final IInstanceService instanceService;
    private final IInstanceRedisService instanceRedisService;
    private final IInstanceRocketmqService instanceRocketmqService;

    /**
     * 存储
     *
     * @param trigger   触发器配置
     * @param eventDesc 事件说明
     */
    public void store(Trigger trigger, String eventDesc) {
        try {


            StringBuffer eventDescSb = new StringBuffer();

            Instance instance = instanceService.getById(trigger.getInstanceId());

            eventDescSb.append("告警主机：").append(instance.getIp());

            Integer type = trigger.getType();
            if (type == 0) {
                InstanceRedis instanceRedis = instanceRedisService.getById(trigger.getInstanceItemId());

                eventDescSb.append("，端口：").append(instanceRedis.getPort());

            } else if (type == 1) {
                InstanceRocketmq instanceRocketmq = instanceRocketmqService.getById(trigger.getInstanceItemId());

                eventDescSb.append("，端口：").append(instanceRocketmq.getPort());
            }

            eventDescSb.append("，告警信息：").append(eventDesc);

            ItemEvent itemEvent = new ItemEvent();
            itemEvent.setInstanceId(trigger.getInstanceId());
            itemEvent.setInstanceItemId(trigger.getInstanceItemId());
            itemEvent.setInstanceType(trigger.getType());
            itemEvent.setItemTriggerId(trigger.getId());
            itemEvent.setEventDesc(eventDescSb.toString());
            itemEventService.add(itemEvent);

        } catch (Exception e) {
            log.error("触发事件存储出现异常", e);
        }
    }

}
