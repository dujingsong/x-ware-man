package cn.imadc.application.xwareman.core.task;

import cn.imadc.application.base.data.structure.RedisInfo;
import cn.imadc.application.base.data.structure.RedisNode;
import cn.imadc.application.base.lettuce.RedisClient;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.item.entity.ItemRedis;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-11
 */
@Slf4j
@AllArgsConstructor
@Component
public class InstanceItemCollectTask {

    private final RedisClient redisClient;
    private final IInstanceRedisService instanceRedisService;
    private final IItemRedisService itemRedisService;

    @Scheduled(initialDelay = 5 * 1000, fixedDelay = 1000)
    public void collectRedisInstanceItem() {

        InstanceRedisFindReqDTO instanceRedisFindReqDTO = new InstanceRedisFindReqDTO();
        List<InstanceRedisData> instanceRedisData = instanceRedisService.listInstanceRedisData(instanceRedisFindReqDTO);

        for (InstanceRedisData theInstanceRedisData : instanceRedisData) {
            try {

                RedisNode redisNode = RedisNode.values()[theInstanceRedisData.getType()];
                String ip = theInstanceRedisData.getIp(), password = theInstanceRedisData.getPassword();
                int port = theInstanceRedisData.getPort();

                RedisInfo redisInfo = instanceRedisService.info(redisNode, ip, port, password);

                ItemRedis itemRedis = new ItemRedis();
                itemRedis.setInstanceId(theInstanceRedisData.getInstanceId());
                itemRedis.setInstanceRedisId(theInstanceRedisData.getId());

                if (!redisNode.equals(RedisNode.SENTINEL)) {
                    RedisInfo.Memory memory = redisInfo.getMemory();
                    itemRedis.setUsedMemory(memory.getUsedMemory());
                }

                RedisInfo.Clients clients = redisInfo.getClients();
                itemRedis.setConnectedClients(clients.getConnectedClients());
                itemRedis.setBlockedClients(clients.getBlockedClients());

                itemRedisService.add(itemRedis);

            } catch (Exception exception) {
                log.error("collectRedisInstanceItem", exception);
            }
        }
    }
}
