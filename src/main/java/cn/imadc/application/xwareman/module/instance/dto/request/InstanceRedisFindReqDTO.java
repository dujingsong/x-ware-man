package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.base.data.structure.redis.RedisNode;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InstanceRedisFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 删除标志
     */
    private Integer delFlag = Constant.NOT_DEL_VAL;

    /**
     * 节点类型
     */
    private RedisNode type;

    /**
     * 节点类型
     */
    private Integer typeVal;

    public Integer getTypeVal() {
        return null != type ? type.getValue() : null;
    }

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 端口
     */
    private Integer port;

    /**
     * master name
     */
    private String masterName;

}
