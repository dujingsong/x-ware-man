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
 * rocketmq????????? ???????????????
 * </p>
 *
 * @author ?????????
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

        // ????????????
        if (null != reqDTO.getType()) {
            queryWrapper.eq("type", reqDTO.getType().getValue());
        }

        // ??????ID
        if (null != reqDTO.getInstanceId()) {
            queryWrapper.eq("instance_id", reqDTO.getInstanceId());
        }

        // ??????
        if (null != reqDTO.getPort()) {
            queryWrapper.eq("port", reqDTO.getPort());
        }

        // ????????????
        if (StringUtils.isNotEmpty(reqDTO.getClusterName())) {
            queryWrapper.eq("cluster_name", reqDTO.getClusterName());
        }

        // ????????????
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

        // ??????namesrv????????????????????????
        String namesrvAddrOne = namesrvAddr.indexOf(Constant.SEMICOLON) > 0
                ? namesrvAddr.split(Constant.SEMICOLON)[0] : namesrvAddr;

        // ????????????rocketmq??????
        DefaultMQAdminExt defaultMQAdminExt = rocketMQClient.getDefaultMQAdminExt(namesrvAddrOne, accessKey, secretKey);

        // ??????????????????
        ClusterInfo clusterInfo = defaultMQAdminExt.examineBrokerClusterInfo();

        // ??????broker??????
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
        // rocketmq????????????????????????????????????
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
     * ??????rocketmq????????????
     *
     * @param infoData ???????????????????????????
     * @param reqDTO   ????????????
     * @param cluster  ??????
     */
    private void doRegisterNode(DiscoveryRocketMQData infoData, InstanceRocketMQRegisterReqDTO reqDTO, Cluster cluster) {

        // ??????ip????????????
        String ip = infoData.getBrokerAddr().split(Constant.COLON)[0];
        int port = Integer.parseInt(infoData.getBrokerAddr().split(Constant.COLON)[1]);
        // ??????????????????
        Instance instance = instanceService.addIfNotExist(new Instance(ip));

        // ????????????
        RocketMQNode rocketMQNode = infoData.getType();

        // ????????????
        String clusterName = infoData.getClusterName();
        // ????????????
        String brokerName = infoData.getBrokerName();

        // ??????????????????????????????
        checkRocketMQInstanceExist(rocketMQNode, instance.getId(), ip, port, clusterName, brokerName);

        // ???????????????rocketmq????????????
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
     * ??????rocketmq???????????????????????????
     *
     * @param type        ????????????
     * @param instanceId  ??????ID
     * @param ip          ip
     * @param port        ??????
     * @param clusterName ????????????
     * @param brokerName  ????????????
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

        String message = String.format("%s????????????ip???%s????????????%s???clusterName???%s???brokerName???%s"
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
     * ????????????
     *
     * @param dataList ????????????
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

        // broker??????
        InstanceRocketmq instanceRocketmq = getById(reqDTO.getId());

        // ????????????
        Instance instance = instanceService.getById(instanceRocketmq.getInstanceId());
        String ip = instance.getIp(), namesrv = instanceRocketmq.getNamesrv();
        String accessKey = instanceRocketmq.getAccessKey(), secretKey = instanceRocketmq.getSecretKey();
        int port = instanceRocketmq.getPort();

        // ????????????
        queryInfoResDTO.setId(instanceRocketmq.getId());
        queryInfoResDTO.setIp(instance.getIp());
        queryInfoResDTO.setPort(instanceRocketmq.getPort());
        queryInfoResDTO.setNodeType(instanceRocketmq.getType());
        queryInfoResDTO.setClusterName(instanceRocketmq.getClusterName());
        queryInfoResDTO.setBrokerName(instanceRocketmq.getBrokerName());

        // ????????????broker??????
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt(namesrv, accessKey, secretKey);

        String brokerAddr = ip + Constant.COLON + port;
        KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(brokerAddr);
        TreeMap<String, String> brokerRuntimeStats = new TreeMap<>(kvTable.getTable());
        queryInfoResDTO.setBrokerRuntimeStats(brokerRuntimeStats);

        // ????????????broker??????
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
