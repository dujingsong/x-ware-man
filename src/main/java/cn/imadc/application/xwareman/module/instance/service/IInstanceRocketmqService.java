package cn.imadc.application.xwareman.module.instance.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRocketMQData;
import cn.imadc.application.xwareman.module.instance.dto.request.DiscoveryRocketMQReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQQueryInfoReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQRegisterReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 * rocketmq实例表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
public interface IInstanceRocketmqService extends IBaseMPService<InstanceRocketmq> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(InstanceRocketMQFindReqDTO reqDTO);

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<InstanceRocketMQData> listInstanceRocketMQData(InstanceRocketMQFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param instanceRocketmq 参数
     * @return 结果
     */
    ResponseW add(InstanceRocketmq instanceRocketmq);

    /**
     * 修改
     *
     * @param instanceRocketmq 参数
     * @return 结果
     */
    ResponseW edit(InstanceRocketmq instanceRocketmq);

    /**
     * 删除
     *
     * @param instanceRocketmq 参数
     * @return 结果
     */
    ResponseW delete(InstanceRocketmq instanceRocketmq);

    /**
     * rocketMQ节点发现
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW discoverRocketMQ(DiscoveryRocketMQReqDTO reqDTO) throws InterruptedException
            , MQClientException, RemotingConnectException, RemotingSendRequestException
            , RemotingTimeoutException, MQBrokerException;

    /**
     * 注册rocketmq节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW register(InstanceRocketMQRegisterReqDTO reqDTO);

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW load(InstanceRocketMQFindReqDTO reqDTO);

    /**
     * 查询rocketmq实例信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW queryInfo(InstanceRocketMQQueryInfoReqDTO reqDTO) throws InterruptedException
            , MQClientException, RemotingConnectException, RemotingSendRequestException
            , RemotingTimeoutException, MQBrokerException, UnsupportedEncodingException;

    /**
     * 获取DefaultMQAdminExt
     *
     * @param instanceRocketMQData 参数
     * @return DefaultMQAdminExt
     */
    DefaultMQAdminExt getDefaultMQAdminExt(InstanceRocketMQData instanceRocketMQData)
            throws InterruptedException, MQClientException;

    /**
     * 获取DefaultMQAdminExt
     *
     * @param namesrv   namesrv地址
     * @param accessKey accessKey
     * @param secretKey secretKey
     * @return DefaultMQAdminExt
     * @throws InterruptedException
     * @throws MQClientException
     */
    DefaultMQAdminExt getDefaultMQAdminExt(String namesrv, String accessKey, String secretKey)
            throws InterruptedException, MQClientException;
}
