package cn.imadc.application.xwareman.basic.rbac.userRole.mapper;

import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import cn.imadc.application.xwareman.basic.rbac.userRole.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 查询用户绑定的角色信息
     *
     * @param userId 用户ID
     * @return 绑定的角色信息
     */
    List<Role> getUserRoleInfo(@Param("userId") Long userId);
}
