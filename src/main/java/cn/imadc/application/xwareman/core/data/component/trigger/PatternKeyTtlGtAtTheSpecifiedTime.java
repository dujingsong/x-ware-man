package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.base.data.structure.redis.RedisNode;
import cn.imadc.application.base.lettuce.RedisClient;
import cn.imadc.application.xwareman.core.data.annoations.StrategyTrigger;
import cn.imadc.application.xwareman.core.data.component.item.InstanceEventStoreComponent;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.data.enums.TriggerStrategyEnum;
import cn.imadc.application.xwareman.core.data.strategy.ITriggerStrategy;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import cn.imadc.application.xwareman.module.trigger.entity.Trigger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 指定时间内匹配的key存活时间超出限额
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@StrategyTrigger(strategy = TriggerStrategyEnum.PATTERN_KEY_TTL_GT_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class PatternKeyTtlGtAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceEventStoreComponent instanceEventStoreComponent;
    private final IInstanceRedisService instanceRedisService;
    private final IInstanceService instanceService;
    private final RedisClient redisClient;

    @Override
    public void handle(Trigger trigger) {
        try {
            // 扫描参数
            JSONObject patternJson = JSON.parseObject(trigger.getPattern());

            String match = patternJson.getString("match");
            Long ttl = patternJson.getLong("ttl");

            // 跳过哨兵节点
            InstanceRedis instanceRedis = instanceRedisService.getById(trigger.getInstanceItemId());

            RedisNode redisNode = RedisNode.of(instanceRedis.getType());
            Instance instance = instanceService.getById(instanceRedis.getInstanceId());

            // 构建scan参数
            ScanArgs scanArgs = new ScanArgs();
            scanArgs.match(match);
            scanArgs.limit(Constant.REDIS_SCAN_COUNT);

            // 查询出key
            List<String> keys = instanceRedisService.scanKey(redisNode, instance.getIp(), instanceRedis.getPort()
                    , instanceRedis.getPassword(), scanArgs);

            if (CollectionUtils.isEmpty(keys)) return;

            // 获取一个连接
            RedisCommands<String, String> redisCommands = redisClient.getRedisCommands(instance.getIp()
                    , instanceRedis.getPort(), instanceRedis.getPassword());

            long ttlOverflowKeyCount = 0;

            // ttl检测
            for (String key : keys) {
                Long remainTtl = redisCommands.ttl(key);
                if (null == remainTtl) continue;

                if (remainTtl < ttl && remainTtl != -1) continue;

                ttlOverflowKeyCount = ttlOverflowKeyCount + 1;
            }

            // 与阈值比较
            long threshold = trigger.getThreshold().longValue();
            if (ttlOverflowKeyCount <= threshold) return;

            String eventDesc = match + "匹配的key存活时间超出限额" + threshold + "实际为" + ttlOverflowKeyCount;

            instanceEventStoreComponent.store(trigger, eventDesc);

        } catch (Exception e) {
            log.error("指定时间内匹配的key存活时间超出限额出现异常", e);
        }
    }
}
