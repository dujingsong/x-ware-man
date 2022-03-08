package cn.imadc.application.xwareman.module.cluster.controller;


import cn.imadc.application.base.common.enums.AuthType;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterFindReqDTO;
import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterQueryReqDTO;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.xwareman.module.cluster.service.IClusterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 集群表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@AllArgsConstructor
@RestController
@RequestMapping("cluster")
public class ClusterController {

    private final IClusterService clusterService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody ClusterFindReqDTO reqDTO) {
        return clusterService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param cluster 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody Cluster cluster) {
        return clusterService.add(cluster);
    }

    /**
     * 修改
     *
     * @param cluster 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody Cluster cluster) {
        return clusterService.edit(cluster);
    }

    /**
     * 删除
     *
     * @param cluster 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody Cluster cluster) {
        return clusterService.delete(cluster);
    }

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @Api(authType = AuthType.ANONYMOUS)
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public ResponseW query(@RequestBody ClusterQueryReqDTO reqDTO) {
        return clusterService.query(reqDTO);
    }
}
