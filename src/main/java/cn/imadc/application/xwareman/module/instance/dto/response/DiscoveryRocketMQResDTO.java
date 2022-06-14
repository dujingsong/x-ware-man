package cn.imadc.application.xwareman.module.instance.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRocketMQData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * rocketMQ节点发现
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class DiscoveryRocketMQResDTO extends BaseResponseDTO {

    /**
     * 发现的broker信息
     */
    private List<DiscoveryRocketMQData> rocketMQInfoData;

}
