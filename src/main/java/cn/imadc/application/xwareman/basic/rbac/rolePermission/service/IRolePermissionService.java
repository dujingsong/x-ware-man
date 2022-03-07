package cn.imadc.application.xwareman.basic.rbac.rolePermission.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.dto.request.RolePermissionFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.entity.RolePermission;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
public interface IRolePermissionService extends IBaseMPService<RolePermission> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(RolePermissionFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param rolePermission 参数
     * @return 结果
     */
    ResponseW add(RolePermission rolePermission);

    /**
     * 修改
     *
     * @param rolePermission 参数
     * @return 结果
     */
    ResponseW edit(RolePermission rolePermission);

    /**
     * 删除
     *
     * @param rolePermission 参数
     * @return 结果
     */
    ResponseW delete(RolePermission rolePermission);
}
