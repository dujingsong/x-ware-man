package cn.imadc.application.xwareman.module.item.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ItemRedisFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 实例redisID
     */
    private Long instanceRedisId;
}
