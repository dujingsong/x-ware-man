package cn.imadc.application.xwareman.basic.rbac.role.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.basic.rbac.role.dto.request.RoleFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import cn.imadc.application.xwareman.basic.rbac.role.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@RestController
@RequestMapping("role")
public class RoleController {

    private final IRoleService roleService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody RoleFindReqDTO reqDTO) {
        return roleService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param role 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody Role role) {
        return roleService.add(role);
    }

    /**
     * 修改
     *
     * @param role 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody Role role) {
        return roleService.edit(role);
    }

    /**
     * 删除
     *
     * @param role 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody Role role) {
        return roleService.delete(role);
    }
}
