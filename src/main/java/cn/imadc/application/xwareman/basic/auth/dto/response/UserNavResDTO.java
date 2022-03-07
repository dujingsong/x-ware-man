package cn.imadc.application.xwareman.basic.auth.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import cn.imadc.application.xwareman.basic.rbac.permission.entity.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 查询用户菜单响应体
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Getter
@Setter
public class UserNavResDTO extends BaseResponseDTO {

    /**
     * 授权的菜单信息
     */
    private List<Permission> permissions;
}
