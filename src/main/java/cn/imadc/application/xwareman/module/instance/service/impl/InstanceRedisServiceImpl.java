package cn.imadc.application.xwareman.module.instance.service.impl;

import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.redis.*;
import cn.imadc.application.base.data.structure.redis.util.RedisStructureUtil;
import cn.imadc.application.base.lettuce.RedisClient;
import cn.imadc.application.base.lettuce.RedisSentinelExtensionCommands;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.base.toolkit.serialization.JsonUtil;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.core.util.MixAllUtil;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.xwareman.module.cluster.service.IClusterService;
import cn.imadc.application.xwareman.module.instance.dto.data.*;
import cn.imadc.application.xwareman.module.instance.dto.request.*;
import cn.imadc.application.xwareman.module.instance.dto.response.DiscoveryRedisResDTO;
import cn.imadc.application.xwareman.module.instance.dto.response.InstanceRedisQueryInfoResDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import cn.imadc.application.xwareman.module.instance.mapper.InstanceRedisMapper;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import cn.imadc.application.xwareman.module.zone.service.IZoneService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * redis????????? ???????????????
 * </p>
 *
 * @author ?????????
 * @since 2022-03-08
 */
@AllArgsConstructor
@Service
public class InstanceRedisServiceImpl extends BaseMPServiceImpl<InstanceRedisMapper, InstanceRedis> implements IInstanceRedisService {

    private final IZoneService zoneService;
    private final IInstanceService instanceService;
    private final RedisClient redisClient;
    private final IClusterService clusterService;
    private final InstanceRedisMapper instanceRedisMapper;

    @Override
    public ResponseW find(InstanceRedisFindReqDTO reqDTO) {
        QueryWrapper<InstanceRedis> queryWrapper = buildQueryWrapper(reqDTO);

        List<InstanceRedis> dataList = list(queryWrapper);
        handle(dataList);
        if (!reqDTO.pageQuery()) return ResponseW.success(dataList);

        Page<InstanceRedis> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<InstanceRedis> pageData = page(page, queryWrapper);
        handle(pageData.getRecords());
        return ResponseW.success(pageData);
    }

    @Override
    public ResponseW load(InstanceRedisFindReqDTO reqDTO) {
        Page<InstanceRedis> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        Page<InstanceRedis> pageData = instanceRedisMapper.loaInstanceRedisData(reqDTO, page);
        handle(pageData.getRecords());
        return ResponseW.success(pageData);
    }

    /**
     * ????????????
     *
     * @param dataList ????????????
     */
    private void handle(List<InstanceRedis> dataList) {
        for (InstanceRedis instanceRedis : dataList) {
            if (null != instanceRedis.getInstanceId()) {
                Instance instance = instanceService.getById(instanceRedis.getInstanceId());
                instanceRedis.setInstance(instance);
                if (null != instance && null != instance.getZoneId()) {
                    instanceRedis.setZone(zoneService.getById(instance.getZoneId()));
                }
            }
            if (null != instanceRedis.getClusterId()) {
                instanceRedis.setCluster(clusterService.getById(instanceRedis.getClusterId()));
            }
        }
    }

    @Override
    public List<InstanceRedis> list(InstanceRedisFindReqDTO reqDTO) {
        QueryWrapper<InstanceRedis> queryWrapper = buildQueryWrapper(reqDTO);
        return list(queryWrapper);
    }

    private QueryWrapper<InstanceRedis> buildQueryWrapper(InstanceRedisFindReqDTO reqDTO) {
        QueryWrapper<InstanceRedis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        // ????????????
        if (null != reqDTO.getType()) {
            queryWrapper.eq("type", reqDTO.getType().getValue());
        }
        // ??????ID
        if (null != reqDTO.getInstanceId()) {
            queryWrapper.eq("instance_id", reqDTO.getInstanceId());
        }
        // ????????????
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

    @Override
    public ResponseW discoverRedis(DiscoveryRedisReqDTO reqDTO) throws InterruptedException {
        DiscoveryRedisResDTO discoveryRedisResDTO = new DiscoveryRedisResDTO();

        List<DiscoveryRedisData> redisInfoDataList = new ArrayList<>();

        // command tool
        RedisSentinelCommands<String, String> sentinelCommands = redisClient.getRedisSentinelCommands(
                reqDTO.getSentinelIp(), reqDTO.getSentinelPort(), reqDTO.getSentinelPassword());
        RedisSentinelExtensionCommands sentinelExtensionCommands = redisClient.getRedisSentinelExtensionCommands(
                reqDTO.getSentinelIp(), reqDTO.getSentinelPort(), reqDTO.getSentinelPassword());

        // ????????????
        List<Map<String, String>> masters = sentinelCommands.masters();
        // ??????????????????????????????
        List<RedisSentinelMaster> sentinelMasters = RedisParser.parseSentinelMasters(JsonUtil.objectToJson(masters));

        DiscoveryRedisData discoveryRedisData;
        List<CommonRedisInfoData> sentinels;
        List<CommonRedisInfoData> dataNodes;
        CommonRedisInfoData commonRedisInfoData;

        for (RedisSentinelMaster redisSentinelMaster : sentinelMasters) {
            discoveryRedisData = new DiscoveryRedisData();
            discoveryRedisData.setMasterName(redisSentinelMaster.getName());

            // ????????????
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

            // ?????????
            dataNodes = new ArrayList<>();
            commonRedisInfoData = new CommonRedisInfoData();
            commonRedisInfoData.setType(RedisNode.MASTER);
            commonRedisInfoData.setIp(redisSentinelMaster.getIp());
            commonRedisInfoData.setPort(redisSentinelMaster.getPort());
            dataNodes.add(commonRedisInfoData);

            // ?????????
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

    @Transactional
    @Override
    public ResponseW register(InstanceRedisRegisterReqDTO reqDTO) {
        // redis????????????????????????????????????
        List<DiscoveryRedisRegisterData> registerNodeData = reqDTO.getRegisterNodeData();

        for (DiscoveryRedisRegisterData nodeData : registerNodeData) {
            Cluster cluster = new Cluster();
            cluster.setName(nodeData.getClusterName());
            cluster.setType(0);
            clusterService.add(cluster);

            nodeData.getNodeInfoData().forEach(infoData -> doRegisterNode(infoData, nodeData, reqDTO, cluster));
        }

        return ResponseW.success();
    }

    /**
     * ??????redis????????????
     *
     * @param infoData ???????????????????????????
     * @param nodeData redis????????????????????????????????????
     * @param reqDTO   redis????????????????????????
     * @param cluster  ????????????
     */
    private void doRegisterNode(DiscoveryRedisRegisterInfoData infoData
            , DiscoveryRedisRegisterData nodeData
            , InstanceRedisRegisterReqDTO reqDTO
            , Cluster cluster
    ) {

        // ????????????
        String masterName = nodeData.getMasterName();
        // ????????????
        String sentinelPassword = reqDTO.getSentinelPassword();
        // ??????????????????
        String dataNodePassword = reqDTO.getNodePassword();
        // ??????ID
        Long clusterId = cluster.getId();

        // ??????????????????
        Instance instance = instanceService.addIfNotExist(new Instance(infoData.getIp()));

        // redis???????????????
        RedisNode redisNode = infoData.getType();

        // ??????????????????????????????
        checkRedisInstanceExist(redisNode, instance.getId(), infoData.getIp(), infoData.getPort(), masterName);

        // ??????????????????????????????????????????????????????
        String password = redisNode.equals(RedisNode.SENTINEL) ? sentinelPassword : dataNodePassword;

        // ???????????????redis????????????
        InstanceRedis instanceRedis = new InstanceRedis();
        instanceRedis.setPort(infoData.getPort());
        instanceRedis.setType(infoData.getType().getValue());
        instanceRedis.setMasterName(masterName);
        instanceRedis.setPassword(password);
        instanceRedis.setInstanceId(instance.getId());
        instanceRedis.setClusterId(clusterId);

        // ??????info?????????????????????redis????????????
        RedisInfo redisInfo = info(redisNode, infoData.getIp(), infoData.getPort(), password);

        // Server
        RedisInfo.Server server = redisInfo.getServer();
        instanceRedis.setRedisVersion(server.getRedisVersion());
        instanceRedis.setProcessId(server.getProcessId());
        instanceRedis.setRunId(server.getRunId());
        instanceRedis.setUptimeInSeconds(server.getUptimeInSeconds());
        instanceRedis.setHz(server.getHz());
        instanceRedis.setExecutable(server.getExecutable());
        instanceRedis.setConfigFile(server.getConfigFile());

        // Memory
        if (!redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Memory memory = redisInfo.getMemory();
            instanceRedis.setMaxMemory(memory.getMaxMemory());
        }

        add(instanceRedis);
    }

    /**
     * ??????redis???????????????????????????
     *
     * @param type       ????????????
     * @param instanceId ??????ID
     * @param ip         ip
     * @param port       ??????
     * @param masterName ????????????
     */
    private void checkRedisInstanceExist(RedisNode type, Long instanceId, String ip, int port, String masterName) {

        InstanceRedisFindReqDTO instanceRedisFindReqDTO = new InstanceRedisFindReqDTO();
        instanceRedisFindReqDTO.setType(type);
        instanceRedisFindReqDTO.setInstanceId(instanceId);
        instanceRedisFindReqDTO.setPort(port);
        instanceRedisFindReqDTO.setMasterName(masterName);
        QueryWrapper<InstanceRedis> queryWrapper = buildQueryWrapper(instanceRedisFindReqDTO);
        long count = count(queryWrapper);
        if (count == 0) return;

        String message = String.format("%s????????????ip???%s????????????%s???masterName???%s", type.getDesc(), ip, port, masterName);
        throw new BizException(message);
    }

    @Override
    public ResponseW queryClusterInfo(InstanceRedisQueryClusterInfoReqDTO reqDTO) {
        List<InstanceRedisClusterInfoData> clusterInfoData = instanceRedisMapper.queryClusterInfo(reqDTO);
        return ResponseW.success(clusterInfoData);
    }

    @Override
    public RedisInfo info(RedisNode redisNode, String ip, int port, String password) {
        String info = infoStr(redisNode, ip, port, password);
        return RedisParser.parseRedisInfo(info);
    }

    @Override
    public Map<String, Map<String, String>> infoMap(RedisNode redisNode, String ip, int port, String password) {
        String info = infoStr(redisNode, ip, port, password);
        return RedisParser.redisInfo(info);
    }

    @Override
    public String infoStr(RedisNode redisNode, String ip, int port, String password) {
        String info;
        try {

            if (redisNode.equals(RedisNode.SENTINEL)) {
                RedisSentinelCommands<String, String> sentinelCommands = redisClient.getRedisSentinelCommands(
                        ip, port, password);

                info = sentinelCommands.info();
            } else {
                RedisCommands<String, String> redisCommands = redisClient.getRedisCommands(ip, port, password);

                info = redisCommands.info();
            }
        } catch (InterruptedException e) {
            throw new BizException("??????redis????????????????????????");
        }

        return info;
    }

    @Override
    public List<InstanceRedisData> listInstanceRedisData(InstanceRedisFindReqDTO reqDTO) {
        return instanceRedisMapper.listInstanceRedisData(reqDTO);
    }

    @Override
    public void refreshInfo(InstanceRedisData instanceRedisData, RedisInfo redisInfo) {
        LambdaUpdateWrapper<InstanceRedis> updateWrapper = Wrappers.lambdaUpdate(InstanceRedis.class);
        updateWrapper.eq(InstanceRedis::getId, instanceRedisData.getId());

        RedisInfo.Server server = redisInfo.getServer();

        // ????????????
        RedisNode redisNode = RedisStructureUtil.parseRedisNode(redisInfo);
        updateWrapper.set(InstanceRedis::getType, redisNode.getValue());

        // ????????????
        if (!redisNode.equals(RedisNode.SENTINEL)) {
            RedisInfo.Memory memory = redisInfo.getMemory();
            updateWrapper.set(InstanceRedis::getMaxMemory, memory.getMaxMemory());
        }

        // ????????????
        updateWrapper.set(InstanceRedis::getRedisVersion, server.getRedisVersion());

        // ??????ID
        updateWrapper.set(InstanceRedis::getProcessId, server.getProcessId());

        // run_id
        updateWrapper.set(InstanceRedis::getRunId, server.getRunId());

        // ????????????
        updateWrapper.set(InstanceRedis::getUptimeInSeconds, server.getUptimeInSeconds());

        // ????????????
        updateWrapper.set(InstanceRedis::getExecutable, server.getExecutable());

        // ????????????????????????
        updateWrapper.set(InstanceRedis::getConfigFile, server.getConfigFile());

        update(updateWrapper);
    }

    @Override
    public ResponseW queryInfo(InstanceRedisQueryInfoReqDTO reqDTO) {

        // redis??????
        InstanceRedis instanceRedis = getById(reqDTO.getId());
        Integer type = instanceRedis.getType();
        RedisNode redisNode = RedisNode.of(type);

        // ????????????
        Instance instance = instanceService.getById(instanceRedis.getInstanceId());
        String ip = instance.getIp(), password = instanceRedis.getPassword();
        int port = instanceRedis.getPort();

        // ???????????????redis????????????
        Map<String, Map<String, String>> infoMap = infoMap(redisNode, ip, port, password);
        RedisInfo redisInfo = RedisParser.parseRedisInfo(infoMap);
        RedisInfo.Server server = redisInfo.getServer();
        RedisInfo.Memory memory = redisInfo.getMemory();
        RedisInfo.Clients clients = redisInfo.getClients();
        RedisInfo.Stats stats = redisInfo.getStats();
        RedisInfo.Keyspace keyspace = redisInfo.getKeyspace();

        // ???????????????????????????
        InstanceRedisQueryInfoResDTO queryInfoResDTO = new InstanceRedisQueryInfoResDTO();
        queryInfoResDTO.setId(instanceRedis.getId());
        queryInfoResDTO.setIp(ip);
        queryInfoResDTO.setPort(port);

        queryInfoResDTO.setPid(server.getProcessId());
        queryInfoResDTO.setOs(server.getOs());
        String memoryDesc = (null != memory
                ? MixAllUtil.prettyMemory(memory.getMaxMemory())
                : Constant.QUESTION_MARK) + Constant.SLASH +
                (null != instance.getMemory()
                        ? instance.getMemory() + "gb"
                        : Constant.QUESTION_MARK
                );
        queryInfoResDTO.setMemoryDesc(memoryDesc);
        queryInfoResDTO.setNotes(instanceRedis.getNotes());
        queryInfoResDTO.setNodeType(instanceRedis.getType());
        queryInfoResDTO.setVersion(server.getRedisVersion());
        queryInfoResDTO.setStartedTime(MixAllUtil.prettyTime(server.getUptimeInSeconds()));
        if (null != memory) {
            queryInfoResDTO.setCurrentMemory(MixAllUtil.prettyMemory(memory.getUsedMemory()));
            queryInfoResDTO.setPeakMemory(MixAllUtil.prettyMemory(memory.getUsedMemoryPeak()));
            queryInfoResDTO.setLuaMemory(MixAllUtil.prettyMemory(memory.getUsedMemoryLua()));
            queryInfoResDTO.setCurrentMemoryRss(MixAllUtil.prettyMemory(memory.getUsedMemoryRss()));
        }
        queryInfoResDTO.setCurrentConnection(clients.getConnectedClients());
        queryInfoResDTO.setTotalConnection(stats.getTotalConnectionsReceived());
        queryInfoResDTO.setTotalCommand(stats.getTotalCommandsProcessed());
        queryInfoResDTO.setBlockedConnection(clients.getBlockedClients());
        if (null != keyspace) {
            List<RedisInfo.Keyspace.DBInfo> dbInfoList = keyspace.getDbInfoList();
            if (!CollectionUtils.isEmpty(dbInfoList)) {
                dbInfoList.sort(Comparator.comparingInt(RedisInfo.Keyspace.DBInfo::getDbIndex));
            }
            queryInfoResDTO.setDbInfoList(dbInfoList);
        }

        // info??????
        queryInfoResDTO.setAllInfo(infoMap);

        return ResponseW.success(queryInfoResDTO);
    }

    @Override
    public void scanKey(KeyScanCursor<String> keyScanCursor, List<String> keyList, RedisCommands<String, String> redisCommands) {
        if (keyScanCursor.getCursor().equals("0")) return;

        keyScanCursor = redisCommands.scan(keyScanCursor);

        keyList.addAll(keyScanCursor.getKeys());

        scanKey(keyScanCursor, keyList, redisCommands);
    }

    @Override
    public List<String> scanKey(RedisNode redisNode, String ip, int port, String password, ScanArgs scanArgs) throws InterruptedException {
        if (redisNode.equals(RedisNode.SENTINEL)) return new ArrayList<>();

        // ??????????????????
        RedisCommands<String, String> redisCommands = redisClient.getRedisCommands(ip, port, password);

        // ??????
        KeyScanCursor<String> keyScanCursor = redisCommands.scan(scanArgs);
        List<String> keys = keyScanCursor.getKeys();

        // ?????????????????????key
        scanKey(keyScanCursor, keys, redisCommands);

        return keys;
    }
}
