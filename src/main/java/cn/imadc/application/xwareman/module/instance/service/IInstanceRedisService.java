package cn.imadc.application.xwareman.module.instance.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.data.structure.redis.RedisInfo;
import cn.imadc.application.base.data.structure.redis.RedisNode;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.instance.dto.request.DiscoveryRedisReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisQueryClusterInfoReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisRegisterReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;

import java.util.List;

/**
 * <p>
 * redis实例表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
public interface IInstanceRedisService extends IBaseMPService<InstanceRedis> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(InstanceRedisFindReqDTO reqDTO);

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW load(InstanceRedisFindReqDTO reqDTO);

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<InstanceRedis> list(InstanceRedisFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param instanceRedis 参数
     * @return 结果
     */
    ResponseW add(InstanceRedis instanceRedis);

    /**
     * 修改
     *
     * @param instanceRedis 参数
     * @return 结果
     */
    ResponseW edit(InstanceRedis instanceRedis);

    /**
     * 删除
     *
     * @param instanceRedis 参数
     * @return 结果
     */
    ResponseW delete(InstanceRedis instanceRedis);

    /**
     * redis节点发现
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW discoverRedis(DiscoveryRedisReqDTO reqDTO) throws InterruptedException;

    /**
     * 注册redis节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW register(InstanceRedisRegisterReqDTO reqDTO);

    /**
     * 查询redis集群信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW queryClusterInfo(InstanceRedisQueryClusterInfoReqDTO reqDTO);

    /**
     * 获取节点的info信息
     *
     * @param redisNode 节点类型
     * @param ip        ip
     * @param port      端口
     * @param password  密码
     * @return 节点的info信息
     */
    RedisInfo info(RedisNode redisNode, String ip, int port, String password);

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<InstanceRedisData> listInstanceRedisData(InstanceRedisFindReqDTO reqDTO);

    /**
     * 刷新节点信息
     *
     * @param instanceRedisData 节点
     * @param redisInfo         节点的info信息
     */
    void refreshInfo(InstanceRedisData instanceRedisData, RedisInfo redisInfo);
}
