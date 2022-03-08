package cn.imadc.application.xwareman.module.instance.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisRegisterReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;

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
     * 注册redis节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW register(InstanceRedisRegisterReqDTO reqDTO);
}
