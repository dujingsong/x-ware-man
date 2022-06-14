package cn.imadc.application.xwareman.module.instance.mapper;

import cn.imadc.application.xwareman.module.instance.dto.data.InstanceRocketMQData;
import cn.imadc.application.xwareman.module.instance.dto.request.InstanceRocketMQFindReqDTO;
import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * rocketmq实例表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Mapper
public interface InstanceRocketmqMapper extends BaseMapper<InstanceRocketmq> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @param page   分页信息
     * @return 查询结果
     */
    Page<InstanceRocketmq> loaInstanceRocketMQData(@Param("condition") InstanceRocketMQFindReqDTO reqDTO, Page<InstanceRocketmq> page);

    /**
     * 查询rocketmq实例信息
     *
     * @param reqDTO 参数
     * @return 结果
     */
    List<InstanceRocketMQData> listInstanceRocketMQData(@Param("condition") InstanceRocketMQFindReqDTO reqDTO);
}
