package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 查询参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class InstanceRocketMQQueryInfoReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

}
