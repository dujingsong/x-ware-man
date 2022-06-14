package cn.imadc.application.xwareman.module.instance.dto.data;

import cn.imadc.application.base.data.structure.rocketmq.RocketMQNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 通用rocketmq节点信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class CommonRocketMQInfoData implements Serializable {

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 节点类型
     */
    private RocketMQNode type;
}
