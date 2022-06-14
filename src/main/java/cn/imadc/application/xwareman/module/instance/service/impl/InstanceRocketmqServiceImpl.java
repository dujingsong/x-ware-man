package cn.imadc.application.xwareman.module.instance.service.impl;

import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.rocketmq.RocketMQNode;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.client.RocketMQClient;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.xwareman.module.cluster.service.IClusterService;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRocketMQData;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRocketMQData;
import cn.imadc.application.xwareman.module.instance.dto.request.DiscoveryRocketMQReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQQueryInfoReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQRegisterReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.response.DiscoveryRocketMQResDTO;
import cn.imadc.application.xwareman.module.instance.dto.response.InstanceRocketMQQueryInfoResDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import cn.imadc.application.xwareman.module.instance.mapper.InstanceRocketmqMapper;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRocketmqService;
import cn.imadc.application.xwareman.module.instance.service.IInstanceService;
import cn.imadc.application.xwareman.module.zone.service.IZoneService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.body.ClusterInfo;
import org.apache.rocketmq.common.protocol.body.KVTable;
import org.apache.rocketmq.common.protocol.route.BrokerData;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <p>
 * rocketmq实例表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@AllArgsConstructor
@Service
public class InstanceRocketmqServiceImpl extends BaseMPServiceImpl<InstanceRocketmqMapper, InstanceRocketmq> implements IInstanceRocketmqService {

    private final RocketMQClient rocketMQClient;
    private final IClusterService clusterService;
    private final IInstanceService instanceService;
    private final IZoneService zoneService;
    private final InstanceRocketmqMapper instanceRocketmqMapper;

    @Override
    public ResponseW find(InstanceRocketMQFindReqDTO reqDTO) {
        QueryWrapper<InstanceRocketmq> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<InstanceRocketmq> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<InstanceRocketmq> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<InstanceRocketmq> buildQueryWrapper(InstanceRocketMQFindReqDTO reqDTO) {
        QueryWrapper<InstanceRocketmq> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        // 节点类型
        if (null != reqDTO.getType()) {
            queryWrapper.eq("type", reqDTO.getType().getValue());
        }

        // 实例ID
        if (null != reqDTO.getInstanceId()) {
            queryWrapper.eq("instance_id", reqDTO.getInstanceId());
        }

        // 端口
        if (null != reqDTO.getPort()) {
            queryWrapper.eq("port", reqDTO.getPort());
        }

        // 集群名称
        if (StringUtils.isNotEmpty(reqDTO.getClusterName())) {
            queryWrapper.eq("cluster_name", reqDTO.getClusterName());
        }

        // 分片名称
        if (StringUtils.isNotEmpty(reqDTO.getBrokerName())) {
            queryWrapper.eq("broker_name", reqDTO.getBrokerName());
        }

        return queryWrapper;
    }

    @Override
    public List<InstanceRocketMQData> listInstanceRocketMQData(InstanceRocketMQFindReqDTO reqDTO) {
        return instanceRocketmqMapper.listInstanceRocketMQData(reqDTO);
    }

    @Override
    public ResponseW add(InstanceRocketmq instanceRocketmq) {
        return save(instanceRocketmq) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(InstanceRocketmq instanceRocketmq) {
        return updateById(instanceRocketmq) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(InstanceRocketmq instanceRocketmq) {
        UpdateWrapper<InstanceRocketmq> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", instanceRocketmq.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW discoverRocketMQ(DiscoveryRocketMQReqDTO reqDTO)
            throws InterruptedException, MQClientException
            , RemotingConnectException, RemotingSendRequestException
            , RemotingTimeoutException, MQBrokerException {
        DiscoveryRocketMQResDTO discoveryRocketMQResDTO = new DiscoveryRocketMQResDTO();

        List<DiscoveryRocketMQData> rocketMQInfoData = new ArrayList<>();
        DiscoveryRocketMQData discoveryRocketMQData;

        String namesrvAddr = reqDTO.getNamesrvAddr();
        String accessKey = reqDTO.getAccessKey();
        String secretKey = reqDTO.getSecretKey();

        // 多个namesrv地址取出其中一个
        String namesrvAddrOne = namesrvAddr.indexOf(Constant.SEMICOLON) > 0
                ? namesrvAddr.split(Constant.SEMICOLON)[0] : namesrvAddr;

        // 获取一个rocketmq连接
        DefaultMQAdminExt defaultMQAdminExt = rocketMQClient.getDefaultMQAdminExt(namesrvAddrOne, accessKey, secretKey);

        // 获取集群信息
        ClusterInfo clusterInfo = defaultMQAdminExt.examineBrokerClusterInfo();

        // 取出broker信息
        HashMap<String, BrokerData> brokerAddrTable = clusterInfo.getBrokerAddrTable();
        Collection<BrokerData> brokerDataCollection = brokerAddrTable.values();

        for (BrokerData brokerData : brokerDataCollection) {
            HashMap<Long, String> brokerAddrs = brokerData.getBrokerAddrs();

            for (Map.Entry<Long, String> entry : brokerAddrs.entrySet()) {
                discoveryRocketMQData = new DiscoveryRocketMQData();

                discoveryRocketMQData.setClusterName(brokerData.getCluster());
                discoveryRocketMQData.setBrokerName(brokerData.getBrokerName());
                discoveryRocketMQData.setBrokerAddr(entry.getValue());
                discoveryRocketMQData.setType(entry.getKey() == 0 ? RocketMQNode.MASTER : RocketMQNode.SLAVE);

                rocketMQInfoData.add(discoveryRocketMQData);
            }
        }

        discoveryRocketMQResDTO.setRocketMQInfoData(rocketMQInfoData);

        return ResponseW.success(discoveryRocketMQResDTO);
    }

    @Transactional
    @Override
    public ResponseW register(InstanceRocketMQRegisterReqDTO reqDTO) {
        // rocketmq节点发现后的集群注册信息
        List<DiscoveryRocketMQData> registerNodeData = reqDTO.getRegisterNodeData();

        Map<String, List<DiscoveryRocketMQData>> rocketMQDataMap = new HashMap<>();

        for (DiscoveryRocketMQData rocketMQData : registerNodeData) {
            String clusterName = rocketMQData.getClusterName() + Constant.COLON + rocketMQData.getBrokerName();

            List<DiscoveryRocketMQData> discoveryRocketMQData = rocketMQDataMap.get(clusterName);
            if (null == discoveryRocketMQData) discoveryRocketMQData = new ArrayList<>();

            discoveryRocketMQData.add(rocketMQData);

            rocketMQDataMap.put(clusterName, discoveryRocketMQData);
        }

        for (Map.Entry<String, List<DiscoveryRocketMQData>> entry : rocketMQDataMap.entrySet()) {
            Cluster cluster = new Cluster();
            cluster.setName(entry.getKey());
            cluster.setType(1);
            clusterService.add(cluster);

            entry.getValue().forEach(infoData -> doRegisterNode(infoData, reqDTO, cluster));
        }


        return ResponseW.success();
    }

    /**
     * 注册rocketmq节点信息
     *
     * @param infoData 集群中单个节点信息
     * @param reqDTO   请求参数
     * @param cluster  集群
     */
    private void doRegisterNode(DiscoveryRocketMQData infoData, InstanceRocketMQRegisterReqDTO reqDTO, Cluster cluster) {

        // 节点ip以及端口
        String ip = infoData.getBrokerAddr().split(Constant.COLON)[0];
        int port = Integer.parseInt(infoData.getBrokerAddr().split(Constant.COLON)[1]);
        // 添加实例信息
        Instance instance = instanceService.addIfNotExist(new Instance(ip));

        // 节点类型
        RocketMQNode rocketMQNode = infoData.getType();

        // 集群名称
        String clusterName = infoData.getClusterName();
        // 分片名称
        String brokerName = infoData.getBrokerName();

        // 判断节点是否重复注册
        checkRocketMQInstanceExist(rocketMQNode, instance.getId(), ip, port, clusterName, brokerName);

        // 构建并存储rocketmq节点信息
        InstanceRocketmq instanceRocketmq = new InstanceRocketmq();
        instanceRocketmq.setInstanceId(instance.getId());
        instanceRocketmq.setClusterId(cluster.getId());
        instanceRocketmq.setPort(port);
        instanceRocketmq.setType(rocketMQNode.getValue());
        instanceRocketmq.setAccessKey(reqDTO.getAccessKey());
        instanceRocketmq.setSecretKey(reqDTO.getSecretKey());
        instanceRocketmq.setClusterName(clusterName);
        instanceRocketmq.setBrokerName(brokerName);
        instanceRocketmq.setNamesrv(reqDTO.getNamesrvAddr());

        add(instanceRocketmq);
    }

    /**
     * 判断rocketmq节点信息是否已存在
     *
     * @param type        节点类型
     * @param instanceId  实例ID
     * @param ip          ip
     * @param port        端口
     * @param clusterName 集群名称
     * @param brokerName  分片名称
     */
    private void checkRocketMQInstanceExist(RocketMQNode type, Long instanceId, String ip, int port
            , String clusterName, String brokerName) {

        InstanceRocketMQFindReqDTO instanceRocketMQFindReqDTO = new InstanceRocketMQFindReqDTO();
        instanceRocketMQFindReqDTO.setType(type);
        instanceRocketMQFindReqDTO.setInstanceId(instanceId);
        instanceRocketMQFindReqDTO.setPort(port);
        instanceRocketMQFindReqDTO.setClusterName(clusterName);
        instanceRocketMQFindReqDTO.setBrokerName(brokerName);
        QueryWrapper<InstanceRocketmq> queryWrapper = buildQueryWrapper(instanceRocketMQFindReqDTO);
        long count = count(queryWrapper);
        if (count == 0) return;

        String message = String.format("%s已存在，ip：%s，端口：%s，clusterName：%s，brokerName：%s"
                , type.getDesc(), ip, port, clusterName, brokerName);
        throw new BizException(message);
    }

    @Override
    public ResponseW load(InstanceRocketMQFindReqDTO reqDTO) {
        Page<InstanceRocketmq> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        Page<InstanceRocketmq> pageData = instanceRocketmqMapper.loaInstanceRocketMQData(reqDTO, page);
        handle(pageData.getRecords());
        return ResponseW.success(pageData);
    }

    /**
     * 处理数据
     *
     * @param dataList 数据列表
     */
    private void handle(List<InstanceRocketmq> dataList) {
        for (InstanceRocketmq instanceRocketmq : dataList) {
            if (null != instanceRocketmq.getInstanceId()) {
                Instance instance = instanceService.getById(instanceRocketmq.getInstanceId());
                instanceRocketmq.setInstance(instance);
                if (null != instance && null != instance.getZoneId()) {
                    instanceRocketmq.setZone(zoneService.getById(instance.getZoneId()));
                }
            }
            if (null != instanceRocketmq.getClusterId()) {
                instanceRocketmq.setCluster(clusterService.getById(instanceRocketmq.getClusterId()));
            }
        }
    }

    @Override
    public ResponseW queryInfo(InstanceRocketMQQueryInfoReqDTO reqDTO) throws InterruptedException
            , MQClientException, RemotingConnectException, RemotingSendRequestException
            , RemotingTimeoutException, MQBrokerException, UnsupportedEncodingException {

        InstanceRocketMQQueryInfoResDTO queryInfoResDTO = new InstanceRocketMQQueryInfoResDTO();

        // broker实例
        InstanceRocketmq instanceRocketmq = getById(reqDTO.getId());

        // 所在实例
        Instance instance = instanceService.getById(instanceRocketmq.getInstanceId());
        String ip = instance.getIp(), namesrv = instanceRocketmq.getNamesrv();
        String accessKey = instanceRocketmq.getAccessKey(), secretKey = instanceRocketmq.getSecretKey();
        int port = instanceRocketmq.getPort();

        // 实例信息
        queryInfoResDTO.setId(instanceRocketmq.getId());
        queryInfoResDTO.setIp(instance.getIp());
        queryInfoResDTO.setPort(instanceRocketmq.getPort());
        queryInfoResDTO.setNodeType(instanceRocketmq.getType());
        queryInfoResDTO.setClusterName(instanceRocketmq.getClusterName());
        queryInfoResDTO.setBrokerName(instanceRocketmq.getBrokerName());

        // 实时拉取broker信息
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt(namesrv, accessKey, secretKey);

        String brokerAddr = ip + Constant.COLON + port;
        KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(brokerAddr);
        TreeMap<String, String> brokerRuntimeStats = new TreeMap<>(kvTable.getTable());
        queryInfoResDTO.setBrokerRuntimeStats(brokerRuntimeStats);

        // 实时拉取broker配置
        Properties properties = defaultMQAdminExt.getBrokerConfig(brokerAddr);
        TreeMap<Object, Object> brokerConfig = new TreeMap<>();
        for (Object key : properties.keySet()) {
            brokerConfig.put(key, properties.get(key));
        }
        queryInfoResDTO.setBrokerConfig(brokerConfig);

        return ResponseW.success(queryInfoResDTO);
    }

    @Override
    public DefaultMQAdminExt getDefaultMQAdminExt(InstanceRocketMQData instanceRocketMQData)
            throws InterruptedException, MQClientException {
        String namesrv = instanceRocketMQData.getNamesrv();
        String accessKey = instanceRocketMQData.getAccessKey(), secretKey = instanceRocketMQData.getSecretKey();
        return getDefaultMQAdminExt(namesrv, accessKey, secretKey);
    }

    @Override
    public DefaultMQAdminExt getDefaultMQAdminExt(String namesrv, String accessKey, String secretKey)
            throws InterruptedException, MQClientException {
        String namesrvOne = namesrv.indexOf(Constant.SEMICOLON) > 0
                ? namesrv.split(Constant.SEMICOLON)[0]
                : namesrv;
        return rocketMQClient.getDefaultMQAdminExt(namesrvOne, accessKey, secretKey);
    }
}
