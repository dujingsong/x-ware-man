package cn.imadc.application.xwareman.module.event.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ItemEventFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 具体的实例ID（redis、rocketmq）
     */
    private Long instanceItemId;

}
