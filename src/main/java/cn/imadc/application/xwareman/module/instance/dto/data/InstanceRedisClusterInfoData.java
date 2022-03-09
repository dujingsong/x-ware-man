package cn.imadc.application.xwareman.module.instance.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
public class InstanceRedisClusterInfoData implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 集群id
     */
    private Long clusterId;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * 实例ip
     */
    private String ip;

    /**
     * 实例端口
     */
    private Integer port;

    /**
     * 实例类型
     */
    private Integer type;

    /**
     * master name
     */
    private String masterName;

    /**
     * 实例最大内存
     */
    private Long maxMemory;

    /**
     * 实例密码
     */
    private String password;
}
