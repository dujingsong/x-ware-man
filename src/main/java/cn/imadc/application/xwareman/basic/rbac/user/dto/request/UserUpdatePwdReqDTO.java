package cn.imadc.application.xwareman.basic.rbac.user.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 更新密码DTO
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Getter
@Setter
public class UserUpdatePwdReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 原密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
