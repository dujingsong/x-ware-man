package cn.imadc.application.xwareman.module.instance.controller;


import cn.imadc.application.base.common.enums.AuthType;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import cn.imadc.application.xwareman.module.instance.dto.request.DiscoveryRocketMQReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQQueryInfoReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQRegisterReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRocketmqService;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingConnectException;
import org.apache.rocketmq.remoting.exception.RemotingSendRequestException;
import org.apache.rocketmq.remoting.exception.RemotingTimeoutException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * rocketmq实例表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@AllArgsConstructor
@RestController
@RequestMapping("instanceRocketMQ")
public class InstanceRocketmqController {

    private final IInstanceRocketmqService instanceRocketmqService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody InstanceRocketMQFindReqDTO reqDTO) {
        return instanceRocketmqService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param instanceRocketmq 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody InstanceRocketmq instanceRocketmq) {
        return instanceRocketmqService.add(instanceRocketmq);
    }

    /**
     * 修改
     *
     * @param instanceRocketmq 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody InstanceRocketmq instanceRocketmq) {
        return instanceRocketmqService.edit(instanceRocketmq);
    }

    /**
     * 删除
     *
     * @param instanceRocketmq 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody InstanceRocketmq instanceRocketmq) {
        return instanceRocketmqService.delete(instanceRocketmq);
    }

    /**
     * rocketMQ节点发现
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "discover/rocketmq", method = RequestMethod.POST)
    public ResponseW discoverRocketMQ(@RequestBody DiscoveryRocketMQReqDTO reqDTO)
            throws InterruptedException, MQClientException, RemotingConnectException
            , RemotingSendRequestException, RemotingTimeoutException, MQBrokerException {
        return instanceRocketmqService.discoverRocketMQ(reqDTO);
    }

    /**
     * 注册rocketmq节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseW register(@RequestBody InstanceRocketMQRegisterReqDTO reqDTO) {
        return instanceRocketmqService.register(reqDTO);
    }

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseW load(@RequestBody InstanceRocketMQFindReqDTO reqDTO) {
        return instanceRocketmqService.load(reqDTO);
    }

    /**
     * 查询rocketmq实例信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "queryInfo", method = RequestMethod.POST)
    public ResponseW queryInfo(@RequestBody InstanceRocketMQQueryInfoReqDTO reqDTO) throws InterruptedException
            , MQClientException, RemotingConnectException, RemotingSendRequestException
            , RemotingTimeoutException, MQBrokerException, UnsupportedEncodingException {
        return instanceRocketmqService.queryInfo(reqDTO);
    }
}
