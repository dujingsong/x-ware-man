package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

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
public class DiscoveryRocketMQReqDTO extends BaseSearchDTO {

    /**
     * namesrv地址，多个以英文分号分割
     */
    private String namesrvAddr;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;
}
