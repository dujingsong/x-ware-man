package cn.imadc.application.xwareman.module.instance.controller;


import cn.imadc.application.base.common.enums.AuthType;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import cn.imadc.application.xwareman.module.instance.dto.request.*;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import cn.imadc.application.xwareman.module.instance.service.IInstanceRedisService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * redis实例表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@AllArgsConstructor
@RestController
@RequestMapping("instanceRedis")
public class InstanceRedisController {

    private final IInstanceRedisService instanceRedisService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody InstanceRedisFindReqDTO reqDTO) {
        return instanceRedisService.find(reqDTO);
    }

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "load", method = RequestMethod.POST)
    public ResponseW load(@RequestBody InstanceRedisFindReqDTO reqDTO) {
        return instanceRedisService.load(reqDTO);
    }

    /**
     * 添加
     *
     * @param instanceRedis 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody InstanceRedis instanceRedis) {
        return instanceRedisService.add(instanceRedis);
    }

    /**
     * 修改
     *
     * @param instanceRedis 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody InstanceRedis instanceRedis) {
        return instanceRedisService.edit(instanceRedis);
    }

    /**
     * 删除
     *
     * @param instanceRedis 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody InstanceRedis instanceRedis) {
        return instanceRedisService.delete(instanceRedis);
    }

    /**
     * redis节点发现
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "discover/redis", method = RequestMethod.POST)
    public ResponseW discoverRedis(@RequestBody DiscoveryRedisReqDTO reqDTO) throws InterruptedException {
        return instanceRedisService.discoverRedis(reqDTO);
    }

    /**
     * 注册redis节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseW register(@RequestBody InstanceRedisRegisterReqDTO reqDTO) {
        return instanceRedisService.register(reqDTO);
    }

    /**
     * 查询redis集群信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "queryClusterInfo", method = RequestMethod.POST)
    public ResponseW queryClusterInfo(@RequestBody InstanceRedisQueryClusterInfoReqDTO reqDTO) {
        return instanceRedisService.queryClusterInfo(reqDTO);
    }

    /**
     * 查询redis实例信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "queryInfo", method = RequestMethod.POST)
    public ResponseW queryInfo(@RequestBody InstanceRedisQueryInfoReqDTO reqDTO) {
        return instanceRedisService.queryInfo(reqDTO);
    }
}
