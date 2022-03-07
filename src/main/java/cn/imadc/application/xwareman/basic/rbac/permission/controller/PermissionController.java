package cn.imadc.application.xwareman.basic.rbac.permission.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.basic.rbac.permission.dto.request.PermissionFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.permission.entity.Permission;
import cn.imadc.application.xwareman.basic.rbac.permission.service.IPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@RestController
@RequestMapping("permission")
public class PermissionController {

    private final IPermissionService permissionService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody PermissionFindReqDTO reqDTO) {
        return permissionService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param permission 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody Permission permission) {
        return permissionService.add(permission);
    }

    /**
     * 修改
     *
     * @param permission 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody Permission permission) {
        return permissionService.edit(permission);
    }

    /**
     * 删除
     *
     * @param permission 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody Permission permission) {
        return permissionService.delete(permission);
    }
}
