package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRocketMQData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * rocketmq节点注册请求参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class InstanceRocketMQRegisterReqDTO extends BaseSearchDTO {

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

    /**
     * 发现的rocketmq节点信息
     */
    private List<DiscoveryRocketMQData> registerNodeData;

}
