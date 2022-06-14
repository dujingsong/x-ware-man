package cn.imadc.application.xwareman.module.instance.dto.data;

import cn.imadc.application.base.data.structure.rocketmq.RocketMQNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * rocketMQ节点发现信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class DiscoveryRocketMQData implements Serializable {

    /**
     * cluster name
     */
    private String clusterName;

    /**
     * broker name
     */
    private String brokerName;

    /**
     * broker addr
     */
    private String brokerAddr;

    /**
     * 节点类型
     */
    private RocketMQNode type;

}
