package cn.imadc.application.xwareman.module.cluster.service;

import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterQueryReqDTO;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterFindReqDTO;

/**
 * <p>
 * 集群表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
public interface IClusterService extends IBaseMPService<Cluster> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(ClusterFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param cluster 参数
     * @return 结果
     */
    ResponseW add(Cluster cluster);

    /**
     * 修改
     *
     * @param cluster 参数
     * @return 结果
     */
    ResponseW edit(Cluster cluster);

    /**
     * 删除
     *
     * @param cluster 参数
     * @return 结果
     */
    ResponseW delete(Cluster cluster);

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW query(ClusterQueryReqDTO reqDTO);

    /**
     * 查询并转换为树节点
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW tree(ClusterFindReqDTO reqDTO);
}
