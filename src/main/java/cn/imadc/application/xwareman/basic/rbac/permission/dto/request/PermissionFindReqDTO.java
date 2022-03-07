package cn.imadc.application.xwareman.basic.rbac.permission.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 查询菜单信息请求体
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-17
 */
@Getter
@Setter
public class PermissionFindReqDTO extends BaseSearchDTO implements Serializable {

    private List<Long> includeIdList;

}
