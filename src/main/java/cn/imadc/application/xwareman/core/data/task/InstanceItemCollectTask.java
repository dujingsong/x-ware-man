package cn.imadc.application.xwareman.core.data.task;

import cn.imadc.application.xwareman.core.data.component.collector.InstanceItemRedisCollectHandler;
import cn.imadc.application.xwareman.core.data.component.collector.InstanceItemRocketMQCollectHandler;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRocketMQData;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQFindReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRocketmqService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 监控项目收集定时任务
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-11
 */
@Slf4j
@AllArgsConstructor
@Component
public class InstanceItemCollectTask {

    private final IInstanceRedisService instanceRedisService;
    private final IInstanceRocketmqService instanceRocketmqService;
    private final InstanceItemRedisCollectHandler instanceItemRedisCollectHandler;
    private final InstanceItemRocketMQCollectHandler instanceItemRocketMQCollectHandler;

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 30 * 1000)
    public void collectRedisInstanceItem() {

        InstanceRedisFindReqDTO findReqDTO = new InstanceRedisFindReqDTO();
        List<InstanceRedisData> instanceRedisData = instanceRedisService.listInstanceRedisData(findReqDTO);

        instanceRedisData.forEach(instanceItemRedisCollectHandler::handle);
    }

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 30 * 1000)
    public void collectRocketMQInstanceItem() {

        InstanceRocketMQFindReqDTO findReqDTO = new InstanceRocketMQFindReqDTO();
        List<InstanceRocketMQData> instanceRocketmqData = instanceRocketmqService.listInstanceRocketMQData(findReqDTO);

        instanceRocketmqData.forEach(instanceItemRocketMQCollectHandler::handle);
    }
}
