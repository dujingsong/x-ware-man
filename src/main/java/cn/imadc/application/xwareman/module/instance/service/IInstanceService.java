package cn.imadc.application.xwareman.module.instance.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceFindReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.Instance;

/**
 * <p>
 * 实例表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
public interface IInstanceService extends IBaseMPService<Instance> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(InstanceFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param instance 参数
     * @return 结果
     */
    ResponseW add(Instance instance);

    /**
     * 修改
     *
     * @param instance 参数
     * @return 结果
     */
    ResponseW edit(Instance instance);

    /**
     * 删除
     *
     * @param instance 参数
     * @return 结果
     */
    ResponseW delete(Instance instance);

    /**
     * 添加实例
     *
     * @param instance 参数
     * @return 结果
     */
    Instance addIfNotExist(Instance instance);
}
