package cn.imadc.application.xwareman.basic.rbac.permission.mapper;

import cn.imadc.application.xwareman.basic.rbac.permission.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 获取用户的权限
     *
     * @param userId 用户ID
     * @return 权限
     */
    List<Permission> getUserPermission(@Param("userId") Long userId);
}
