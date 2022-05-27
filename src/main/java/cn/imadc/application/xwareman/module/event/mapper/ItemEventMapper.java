package cn.imadc.application.xwareman.module.event.mapper;

import cn.imadc.application.xwareman.module.event.entity.ItemEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 告警记录 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-25
 */
@Mapper
public interface ItemEventMapper extends BaseMapper<ItemEvent> {

}
