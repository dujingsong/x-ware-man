package cn.imadc.application.xwareman.module.cluster.mapper;

import cn.imadc.application.xwareman.module.cluster.dto.data.ClusterQueryData;
import cn.imadc.application.xwareman.module.cluster.dto.request.ClusterQueryReqDTO;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 集群表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Mapper
public interface ClusterMapper extends BaseMapper<Cluster> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<ClusterQueryData> query(@Param("condition") ClusterQueryReqDTO reqDTO);
}
