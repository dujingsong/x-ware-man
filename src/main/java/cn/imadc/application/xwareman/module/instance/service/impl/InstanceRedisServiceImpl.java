package cn.imadc.application.xwareman.module.instance.service.impl;

import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.RedisInfo;
import cn.imadc.application.base.data.structure.RedisNode;
import cn.imadc.application.base.data.structure.RedisParser;
import cn.imadc.application.base.lettuce.RedisClient;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.xwareman.module.cluster.service.IClusterService;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRedisRegisterData;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRedisRegisterInfoData;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisClusterInfoData;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisQueryClusterInfoReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisRegisterReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import cn.imadc.application.xwareman.module.instance.mapper.InstanceRedisMapper;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * redis实例表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@AllArgsConstructor
@Service
public class InstanceRedisServiceImpl extends BaseMPServiceImpl<InstanceRedisMapper, InstanceRedis> implements IInstanceRedisService {

    private final IInstanceService instanceService;
    private final RedisClient redisClient;
    private final IClusterService clusterService;
    private final InstanceRedisMapper instanceRedisMapper;

    @Override
    public ResponseW find(InstanceRedisFindReqDTO reqDTO) {
        QueryWrapper<InstanceRedis> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<InstanceRedis> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<InstanceRedis> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<InstanceRedis> buildQueryWrapper(InstanceRedisFindReqDTO reqDTO) {
        QueryWrapper<InstanceRedis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        // 节点类型
        if (null != reqDTO.getType()) {
            queryWrapper.eq("type", reqDTO.getType().getValue());
        }
        // 实例ID
        if (null != reqDTO.getInstanceId()) {
            queryWrapper.eq("instance_id", reqDTO.getInstanceId());
        }
        // 节点端口
        if (null != reqDTO.getPort()) {
            queryWrapper.eq("port", reqDTO.getPort());
        }
        // master name
        if (StringUtils.isNotBlank(reqDTO.getMasterName())) {
            queryWrapper.eq("master_name", reqDTO.getMasterName());
        }

        return queryWrapper;
    }

    @Override
    public ResponseW add(InstanceRedis instanceRedis) {
        return save(instanceRedis) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(InstanceRedis instanceRedis) {
        return updateById(instanceRedis) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(InstanceRedis instanceRedis) {
        UpdateWrapper<InstanceRedis> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", instanceRedis.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Transactional
    @Override
    public ResponseW register(InstanceRedisRegisterReqDTO reqDTO) {
        List<DiscoveryRedisRegisterData> registerNodeData = reqDTO.getRegisterNodeData();

        for (DiscoveryRedisRegisterData nodeData : registerNodeData) {
            Cluster cluster = new Cluster();
            cluster.setName(nodeData.getClusterName());
            clusterService.add(cluster);

            nodeData.getNodeInfoData().forEach(infoData -> doRegisterNode(infoData
                    , nodeData.getMasterName()
                    , reqDTO.getSentinelPassword()
                    , reqDTO.getNodePassword()
                    , cluster.getId())

            );
        }

        return ResponseW.success();
    }

    private void doRegisterNode(DiscoveryRedisRegisterInfoData nodeData, String masterName
            , String sentinelPassword, String dataNodePassword, Long clusterId) {

        Instance instance = instanceService.addIfNotExist(new Instance(nodeData.getIp()));

        RedisNode redisNode = nodeData.getType();

        checkRedisInstanceExist(redisNode, instance.getId(), nodeData.getIp(), nodeData.getPort(), masterName);

        String password = redisNode.equals(RedisNode.SENTINEL) ? sentinelPassword : dataNodePassword;

        InstanceRedis instanceRedis = new InstanceRedis();
        instanceRedis.setPort(nodeData.getPort());
        instanceRedis.setType(nodeData.getType().getValue());
        instanceRedis.setMasterName(masterName);
        instanceRedis.setPassword(password);
        instanceRedis.setInstanceId(instance.getId());
        instanceRedis.setClusterId(clusterId);

        try {
            String info;
            if (redisNode.equals(RedisNode.SENTINEL)) {
                RedisSentinelCommands<String, String> sentinelCommands = redisClient.getRedisSentinelCommands(
                        nodeData.getIp(), nodeData.getPort(), password);

                info = sentinelCommands.info();
            } else {
                RedisCommands<String, String> redisCommands = redisClient.getRedisCommands(nodeData.getIp()
                        , nodeData.getPort(), password);

                info = redisCommands.info();
            }

            RedisInfo redisInfo = RedisParser.parseRedisInfo(info);
            if (!redisNode.equals(RedisNode.SENTINEL)) {
                RedisInfo.Memory memory = redisInfo.getMemory();
                instanceRedis.setMaxMemory(memory.getMaxMemory());
            }

        } catch (InterruptedException e) {
            throw new BizException("连接redis节点获取信息失败");
        }

        add(instanceRedis);
    }

    private void checkRedisInstanceExist(RedisNode type, Long instanceId, String ip, int port, String masterName) {

        InstanceRedisFindReqDTO instanceRedisFindReqDTO = new InstanceRedisFindReqDTO();
        instanceRedisFindReqDTO.setType(type);
        instanceRedisFindReqDTO.setInstanceId(instanceId);
        instanceRedisFindReqDTO.setPort(port);
        instanceRedisFindReqDTO.setMasterName(masterName);
        QueryWrapper<InstanceRedis> queryWrapper = buildQueryWrapper(instanceRedisFindReqDTO);
        long count = count(queryWrapper);
        if (count == 0) return;

        String message = String.format("%s已存在，ip：%s，端口：%s，masterName：%s", type.getDesc(), ip, port, masterName);
        throw new BizException(message);
    }

    @Override
    public ResponseW queryClusterInfo(InstanceRedisQueryClusterInfoReqDTO reqDTO) {
        List<InstanceRedisClusterInfoData> clusterInfoData = instanceRedisMapper.queryClusterInfo(reqDTO);
        return ResponseW.success(clusterInfoData);
    }
}
