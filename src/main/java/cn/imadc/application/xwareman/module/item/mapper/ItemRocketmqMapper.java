package cn.imadc.application.xwareman.module.item.mapper;

import cn.imadc.application.xwareman.module.item.entity.ItemRocketmq;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * redis监控项数据搜集表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-10
 */
@Mapper
public interface ItemRocketmqMapper extends BaseMapper<ItemRocketmq> {

    List<Object> selectColAtSpecifiedTime(@Param("col") String col, @Param("begin") String begin, @Param("end") String end, @Param("instanceRocketmqId") Long instanceRocketmqId);
}
