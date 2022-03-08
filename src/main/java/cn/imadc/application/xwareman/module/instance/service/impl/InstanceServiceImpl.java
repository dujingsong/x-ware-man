package cn.imadc.application.xwareman.module.instance.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.*;
import cn.imadc.application.base.lettuce.RedisClient;
import cn.imadc.application.base.lettuce.RedisSentinelExtensionCommands;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.base.toolkit.serialization.JsonUtil;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.instance.dto.data.CommonRedisInfoData;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRedisData;
import cn.imadc.application.xwareman.module.instance.dto.request.DiscoveryRedisReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.response.DiscoveryRedisResDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.mapper.InstanceMapper;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 实例表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@AllArgsConstructor
@Service
public class InstanceServiceImpl extends BaseMPServiceImpl<InstanceMapper, Instance> implements IInstanceService {

    private final RedisClient redisClient;

    @Override
    public ResponseW find(InstanceFindReqDTO reqDTO) {
        QueryWrapper<Instance> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<Instance> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Instance> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Instance> buildQueryWrapper(InstanceFindReqDTO reqDTO) {
        QueryWrapper<Instance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        // ip
        if (StringUtils.isNotBlank(reqDTO.getIp())) {
            queryWrapper.eq("ip", reqDTO.getIp());
        }

        return queryWrapper;
    }

    @Override
    public ResponseW add(Instance instance) {
        return save(instance) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Instance instance) {
        return updateById(instance) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Instance instance) {
        UpdateWrapper<Instance> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", instance.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW discoverRedis(DiscoveryRedisReqDTO reqDTO) throws InterruptedException {
        DiscoveryRedisResDTO discoveryRedisResDTO = new DiscoveryRedisResDTO();

        List<DiscoveryRedisData> redisInfoDataList = new ArrayList<>();

        RedisSentinelCommands<String, String> sentinelCommands = redisClient.getRedisSentinelCommands(
                reqDTO.getSentinelIp(), reqDTO.getSentinelPort(), reqDTO.getSentinelPassword());
        RedisSentinelExtensionCommands sentinelExtensionCommands = redisClient.getRedisSentinelExtensionCommands(
                reqDTO.getSentinelIp(), reqDTO.getSentinelPort(), reqDTO.getSentinelPassword());

        List<Map<String, String>> masters = sentinelCommands.masters();
        List<RedisSentinelMaster> sentinelMasters = RedisParser.parseSentinelMasters(JsonUtil.objectToJson(masters));

        DiscoveryRedisData discoveryRedisData;
        List<CommonRedisInfoData> sentinels;
        List<CommonRedisInfoData> dataNodes;
        CommonRedisInfoData commonRedisInfoData;

        for (RedisSentinelMaster redisSentinelMaster : sentinelMasters) {
            discoveryRedisData = new DiscoveryRedisData();
            discoveryRedisData.setMasterName(redisSentinelMaster.getName());

            // 哨兵节点
            sentinels = new ArrayList<>();
            commonRedisInfoData = new CommonRedisInfoData();
            commonRedisInfoData.setType(RedisNode.SENTINEL);
            commonRedisInfoData.setIp(reqDTO.getSentinelIp());
            commonRedisInfoData.setPort(reqDTO.getSentinelPort());
            sentinels.add(commonRedisInfoData);

            List<Map<String, String>> otherSentinels = sentinelExtensionCommands.sentinelSentinels(
                    redisSentinelMaster.getName());
            List<RedisSentinel> otherSentinelData = RedisParser.parseSentinels(JsonUtil.objectToJson(otherSentinels));
            for (RedisSentinel redisSentinel : otherSentinelData) {
                commonRedisInfoData = new CommonRedisInfoData();
                commonRedisInfoData.setType(RedisNode.SENTINEL);
                commonRedisInfoData.setIp(redisSentinel.getIp());
                commonRedisInfoData.setPort(redisSentinel.getPort());
                sentinels.add(commonRedisInfoData);
            }
            discoveryRedisData.setSentinels(sentinels);

            // 主节点
            dataNodes = new ArrayList<>();
            commonRedisInfoData = new CommonRedisInfoData();
            commonRedisInfoData.setType(RedisNode.MASTER);
            commonRedisInfoData.setIp(redisSentinelMaster.getIp());
            commonRedisInfoData.setPort(redisSentinelMaster.getPort());
            dataNodes.add(commonRedisInfoData);

            // 从节点
            List<Map<String, String>> slaveDataNodes = sentinelCommands.slaves(redisSentinelMaster.getName());
            List<RedisSentinelSlave> slaveData = RedisParser.parseSentinelSlaves(JsonUtil.objectToJson(slaveDataNodes));
            for (RedisSentinelSlave redisSentinelSlave : slaveData) {
                commonRedisInfoData = new CommonRedisInfoData();
                commonRedisInfoData.setType(RedisNode.SLAVE);
                commonRedisInfoData.setIp(redisSentinelSlave.getIp());
                commonRedisInfoData.setPort(redisSentinelSlave.getPort());
                dataNodes.add(commonRedisInfoData);
            }
            discoveryRedisData.setDataNodes(dataNodes);

            redisInfoDataList.add(discoveryRedisData);
        }

        discoveryRedisResDTO.setRedisInfoData(redisInfoDataList);

        return ResponseW.success(discoveryRedisResDTO);
    }

    @Override
    public Instance addIfNotExist(Instance instance) {
        InstanceFindReqDTO instanceFindReqDTO = new InstanceFindReqDTO();
        instanceFindReqDTO.setIp(instance.getIp());
        QueryWrapper<Instance> queryWrapper = buildQueryWrapper(instanceFindReqDTO);
        Instance existInstance = getOne(queryWrapper);
        if (null != existInstance) return existInstance;

        add(instance);
        return instance;
    }
}
