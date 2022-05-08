package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 查询集群信息参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
public class InstanceRedisQueryClusterInfoReqDTO extends BaseSearchDTO {

    private Integer delFlag = Constant.NOT_DEL_VAL;

    /**
     * 集群ID
     */
    private Long clusterId;

}
