package cn.imadc.application.xwareman.module.cluster.service.impl;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.impl.BaseMPServiceImpl;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import cn.imadc.application.xwareman.module.cluster.dto.data.ClusterQueryData;
import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterFindReqDTO;
import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterQueryReqDTO;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.xwareman.module.cluster.mapper.ClusterMapper;
import cn.imadc.application.xwareman.module.cluster.service.IClusterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 集群表 服务实现类
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@AllArgsConstructor
@Service
public class ClusterServiceImpl extends BaseMPServiceImpl<ClusterMapper, Cluster> implements IClusterService {

    private final ClusterMapper clusterMapper;

    @Override
    public ResponseW find(ClusterFindReqDTO reqDTO) {
        QueryWrapper<Cluster> queryWrapper = buildQueryWrapper(reqDTO);

        if (!reqDTO.pageQuery()) return ResponseW.success(list(queryWrapper));

        Page<Cluster> page = new Page<>(reqDTO.getPageNo(), reqDTO.getPageSize(), true);
        IPage<Cluster> pageData = page(page, queryWrapper);
        return ResponseW.success(pageData);
    }

    private QueryWrapper<Cluster> buildQueryWrapper(ClusterFindReqDTO reqDTO) {
        QueryWrapper<Cluster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constant.DEL_FLAG, Constant.NOT_DEL_VAL);

        return queryWrapper;
    }

    @Override
    public ResponseW add(Cluster cluster) {
        return save(cluster) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW edit(Cluster cluster) {
        return updateById(cluster) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW delete(Cluster cluster) {
        UpdateWrapper<Cluster> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", cluster.getId());
        userUpdateWrapper.set(Constant.DEL_FLAG, Constant.DEL_VAL);
        return update(userUpdateWrapper) ? ResponseW.success() : ResponseW.error();
    }

    @Override
    public ResponseW query(ClusterQueryReqDTO reqDTO) {
        List<ClusterQueryData> clusters = clusterMapper.query(reqDTO);
        return ResponseW.success(clusters);
    }
}
