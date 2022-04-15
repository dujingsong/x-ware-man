package cn.imadc.application.xwareman.module.instance.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * redis节点发现后的集群注册信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Getter
@Setter
public class DiscoveryRedisRegisterData implements Serializable {

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * master name
     */
    private String masterName;

    /**
     * 节点信息
     */
    private List<DiscoveryRedisRegisterInfoData> nodeInfoData;
}
