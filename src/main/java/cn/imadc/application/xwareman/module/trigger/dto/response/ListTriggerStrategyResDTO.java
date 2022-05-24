package cn.imadc.application.xwareman.module.trigger.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import cn.imadc.application.xwareman.module.trigger.dto.data.TriggerStrategyData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 触发器规则查询响应参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-04-18
 */
@Getter
@Setter
public class ListTriggerStrategyResDTO extends BaseResponseDTO implements Serializable {

    /**
     * 触发器策略
     */
    private List<TriggerStrategyData> triggerStrategyDataList;

}
