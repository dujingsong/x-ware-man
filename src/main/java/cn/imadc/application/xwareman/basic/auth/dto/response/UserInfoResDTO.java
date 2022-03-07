package cn.imadc.application.xwareman.basic.auth.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import cn.imadc.application.xwareman.basic.rbac.role.entity.Role;
import cn.imadc.application.xwareman.basic.rbac.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 查询用户信息响应体
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Getter
@Setter
public class UserInfoResDTO extends BaseResponseDTO {

    /**
     * 用户信息
     */
    private User user;

    /**
     * 角色信息
     */
    private List<Role> roles;
}
