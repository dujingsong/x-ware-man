package cn.imadc.application.xwareman.basic.rbac.role.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 查询角色参数DTO
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Getter
@Setter
public class RoleFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 角色类型（PERM:菜单权限；ITEM：项目权限；ITEM_FLOW:项目流程权限）
     */
    private String type;

}
