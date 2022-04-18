package cn.imadc.application.xwareman.core.task;

import cn.imadc.application.xwareman.core.component.collector.InstanceItemRedisCollectHandler;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
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
    private final InstanceItemRedisCollectHandler instanceItemRedisCollectHandler;

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 1000)
    public void collectRedisInstanceItem() {

        InstanceRedisFindReqDTO findReqDTO = new InstanceRedisFindReqDTO();
        List<InstanceRedisData> instanceRedisData = instanceRedisService.listInstanceRedisData(findReqDTO);

        instanceRedisData.forEach(instanceItemRedisCollectHandler::handle);
    }
}
