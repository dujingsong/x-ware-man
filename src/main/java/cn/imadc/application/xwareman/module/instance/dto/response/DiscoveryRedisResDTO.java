package cn.imadc.application.xwareman.module.instance.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRedisData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * redis节点发现
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Getter
@Setter
public class DiscoveryRedisResDTO extends BaseResponseDTO {

    /**
     * 发现的redis节点信息
     */
    private List<DiscoveryRedisData> redisInfoData;

}
