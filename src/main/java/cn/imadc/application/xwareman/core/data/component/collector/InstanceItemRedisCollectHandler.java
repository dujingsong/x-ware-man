package cn.imadc.application.xwareman.core.data.component.collector;

import cn.imadc.application.base.data.structure.redis.RedisInfo;
import cn.imadc.application.base.data.structure.redis.RedisNode;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.item.service.IItemRedisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <p>
 * redis监控项收集
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-15
 */
@AllArgsConstructor
@Component
public class InstanceItemRedisCollectHandler {

    private final IItemRedisService itemRedisService;
    private final IInstanceRedisService instanceRedisService;

    public void handle(InstanceRedisData instanceRedisData) {

        // 拉取并解析redis节点的info信息
        RedisNode redisNode = RedisNode.values()[instanceRedisData.getType()];
        String ip = instanceRedisData.getIp(), password = instanceRedisData.getPassword();
        int port = instanceRedisData.getPort();

        RedisInfo redisInfo = instanceRedisService.info(redisNode, ip, port, password);

        // 刷新节点信息
        instanceRedisService.refreshInfo(instanceRedisData, redisInfo);

        // 监控项收集
        itemRedisService.storeItemRedis(instanceRedisData, redisInfo);
    }

}
