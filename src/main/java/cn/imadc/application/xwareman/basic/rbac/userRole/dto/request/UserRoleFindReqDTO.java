package cn.imadc.application.xwareman.basic.rbac.userRole.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户角色查询DTO
 */
@Getter
@Setter
public class UserRoleFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

}
