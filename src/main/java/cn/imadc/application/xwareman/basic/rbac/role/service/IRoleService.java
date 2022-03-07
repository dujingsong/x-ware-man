package cn.imadc.application.xwareman.basic.rbac.role.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.basic.rbac.role.dto.request.RoleFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
public interface IRoleService extends IBaseMPService<Role> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(RoleFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param role 参数
     * @return 结果
     */
    ResponseW add(Role role);

    /**
     * 修改
     *
     * @param role 参数
     * @return 结果
     */
    ResponseW edit(Role role);

    /**
     * 删除
     *
     * @param role 参数
     * @return 结果
     */
    ResponseW delete(Role role);

    /**
     * 获取用户绑定的角色信息
     *
     * @param userId 用户ID
     * @return 绑定的角色信息
     */
    List<Role> getUserRole(Long userId);
}
