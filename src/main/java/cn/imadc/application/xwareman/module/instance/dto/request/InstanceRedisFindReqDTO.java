package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.base.data.structure.RedisNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class InstanceRedisFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 节点类型
     */
    private RedisNode type;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 端口
     */
    private Integer port;

    /**
     * master name
     */
    private String masterName;

}
