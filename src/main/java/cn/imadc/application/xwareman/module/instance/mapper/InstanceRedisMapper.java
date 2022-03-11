package cn.imadc.application.xwareman.module.instance.mapper;

import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisClusterInfoData;
import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRedisData;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisFindReqDTO;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRedisQueryClusterInfoReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * redis实例表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Mapper
public interface InstanceRedisMapper extends BaseMapper<InstanceRedis> {

    /**
     * 查询redis集群信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<InstanceRedisClusterInfoData> queryClusterInfo(@Param("condition") InstanceRedisQueryClusterInfoReqDTO reqDTO);

    /**
     * 查询redis实例信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<InstanceRedisData> listInstanceRedisData(@Param("condition") InstanceRedisFindReqDTO reqDTO);
}
