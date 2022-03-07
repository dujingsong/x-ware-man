package cn.imadc.application.xwareman.basic.rbac.user.mapper;

import cn.imadc.application.xwareman.basic.rbac.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
