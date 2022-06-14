package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.base.data.structure.rocketmq.RocketMQNode;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * rocketmq查询参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class InstanceRocketMQFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 删除标志
     */
    private Integer delFlag = Constant.NOT_DEL_VAL;

    /**
     * 节点类型
     */
    private RocketMQNode type;

    /**
     * 节点类型
     */
    private Integer typeVal;

    public Integer getTypeVal() {
        return null != type ? type.getValue() : null;
    }

    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 分片名称
     */
    private String brokerName;
}
