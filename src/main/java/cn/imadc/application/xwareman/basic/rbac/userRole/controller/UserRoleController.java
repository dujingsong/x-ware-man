package cn.imadc.application.xwareman.basic.rbac.userRole.controller;


import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.basic.rbac.userRole.dto.request.UserRoleFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.userRole.entity.UserRole;
import cn.imadc.application.xwareman.basic.rbac.userRole.service.IUserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@AllArgsConstructor
@RestController
@RequestMapping("userRole")
public class UserRoleController {

    private final IUserRoleService userRoleService;

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    @RequestMapping(value = "find", method = RequestMethod.POST)
    public ResponseW find(@RequestBody UserRoleFindReqDTO reqDTO) {
        return userRoleService.find(reqDTO);
    }

    /**
     * 添加
     *
     * @param userRole 参数
     * @return 结果
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseW add(@RequestBody UserRole userRole) {
        return userRoleService.add(userRole);
    }

    /**
     * 修改
     *
     * @param userRole 参数
     * @return 结果
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseW edit(@RequestBody UserRole userRole) {
        return userRoleService.edit(userRole);
    }

    /**
     * 删除
     *
     * @param userRole 参数
     * @return 结果
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseW delete(@RequestBody UserRole userRole) {
        return userRoleService.delete(userRole);
    }
}
