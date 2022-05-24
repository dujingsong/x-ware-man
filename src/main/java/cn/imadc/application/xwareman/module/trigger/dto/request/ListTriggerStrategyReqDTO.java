package cn.imadc.application.xwareman.module.trigger.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 触发器规则查询参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Getter
@Setter
public class ListTriggerStrategyReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * -1 通用； 0 redis； 1 rocketmq
     */
    private Integer scope;

}
