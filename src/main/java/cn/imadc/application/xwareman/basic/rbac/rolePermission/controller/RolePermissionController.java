package cn.imadc.application.xwareman.basic.rbac.rolePermission.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.dto.request.RolePermissionFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.entity.RolePermission;
import cn.imadc.application.xwareman.basic.rbac.rolePermission.service.IRolePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色菜单表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@RestController
@RequestMapping("rolePermission")
public class RolePermissionController {

    private final IRolePermissionService rolePermissionService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody RolePermissionFindReqDTO reqDTO) {
        return rolePermissionService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param rolePermission 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody RolePermission rolePermission) {
        return rolePermissionService.add(rolePermission);
    }

    /**
     * 修改
     *
     * @param rolePermission 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody RolePermission rolePermission) {
        return rolePermissionService.edit(rolePermission);
    }

    /**
     * 删除
     *
     * @param rolePermission 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody RolePermission rolePermission) {
        return rolePermissionService.delete(rolePermission);
    }
}
