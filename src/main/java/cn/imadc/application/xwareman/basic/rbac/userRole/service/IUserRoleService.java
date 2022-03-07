package cn.imadc.application.xwareman.basic.rbac.userRole.service;

import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.base.mybatisplus.repository.IBaseMPService;
import cn.imadc.application.xwareman.basic.rbac.userRole.dto.request.UserRoleFindReqDTO;
import cn.imadc.application.xwareman.basic.rbac.userRole.entity.UserRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
public interface IUserRoleService extends IBaseMPService<UserRole> {

    /**
     * 查询
     *
     * @param reqDTO 参数
     * @return 结果
     */
    ResponseW find(UserRoleFindReqDTO reqDTO);

    /**
     * 添加
     *
     * @param userRole 参数
     * @return 结果
     */
    ResponseW add(UserRole userRole);

    /**
     * 修改
     *
     * @param userRole 参数
     * @return 结果
     */
    ResponseW edit(UserRole userRole);

    /**
     * 删除
     *
     * @param userRole 参数
     * @return 结果
     */
    ResponseW delete(UserRole userRole);

    /**
     * 将用户与角色绑定
     *
     * @param userId  用户ID
     * @param roleIds 角色集合
     * @return 结果
     */
    ResponseW bindUser(Long userId, Set<Long> roleIds);

    /**
     * 获取用户的角色ID
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<Map<String, Object>> getUserRoleInfo(Long userId);
}
