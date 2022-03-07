package cn.imadc.application.xwareman.basic.rbac.role.mapper;

import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取用户绑定的角色信息
     *
     * @param userId 用户ID
     * @return 绑定的角色信息
     */
    List<Role> getUserRole(@Param("userId") Long userId);
}
