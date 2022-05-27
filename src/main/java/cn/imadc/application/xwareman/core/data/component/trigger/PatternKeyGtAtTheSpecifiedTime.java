package cn.imadc.application.xwareman.core.data.component.trigger;

import cn.imadc.application.base.data.structure.redis.RedisNode;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 指定时间内符合条件的数据数量大于
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Slf4j
@StrategyTrigger(strategy = TriggerStrategyEnum.PATTERN_KEY_GT_AT_THE_SPECIFIED_TIME)
@RequiredArgsConstructor
@Component
public class PatternKeyGtAtTheSpecifiedTime implements ITriggerStrategy {

    private final InstanceEventStoreComponent instanceEventStoreComponent;
    private final IInstanceRedisService instanceRedisService;
    private final IInstanceService instanceService;

    @Override
    public void handle(Trigger trigger) {

        try {

            // 跳过哨兵节点
            InstanceRedis instanceRedis = instanceRedisService.getById(trigger.getInstanceItemId());

            RedisNode redisNode = RedisNode.of(instanceRedis.getType());
            Instance instance = instanceService.getById(instanceRedis.getInstanceId());

            // 构建scan参数
            ScanArgs scanArgs = new ScanArgs();
            // 扫描参数
            JSONObject patternJson = JSON.parseObject(trigger.getPattern());
            String match = patternJson.getString("match");
            scanArgs.match(match);
            scanArgs.limit(Constant.REDIS_SCAN_COUNT);

            // 查询出key
            List<String> keys = instanceRedisService.scanKey(redisNode, instance.getIp(), instanceRedis.getPort()
                    , instanceRedis.getPassword(), scanArgs);

            if (CollectionUtils.isEmpty(keys)) return;

            // 与阈值比较
            long threshold = trigger.getThreshold().longValue();
            long matchedKeyCount = keys.size();

            if (matchedKeyCount <= threshold) return;

            String eventDesc = trigger.getPattern() + "匹配的key数量超出限额" + threshold + "实际为" + matchedKeyCount;

            instanceEventStoreComponent.store(trigger, eventDesc);

        } catch (Exception e) {
            log.error("指定时间内符合条件的数据数量大于", e);
        }

    }
}
