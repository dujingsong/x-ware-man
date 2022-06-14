package cn.imadc.application.xwareman.module.instance.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import cn.imadc.application.xwareman.module.cluster.entity.Cluster;
import cn.imadc.application.xwareman.module.zone.entity.Zone;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * rocketmq实例表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
@TableName("instance_rocketmq")
public class InstanceRocketmq extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备注
     */
    private String notes;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 实例信息
     */
    @TableField(exist = false)
    private Instance instance;

    /**
     * 区域信息
     */
    @TableField(exist = false)
    private Zone zone;

    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 集群信息
     */
    @TableField(exist = false)
    private Cluster cluster;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 0：主节点；1：从节点；2：namesrv节点
     */
    private Integer type;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * clusterName
     */
    private String clusterName;

    /**
     * broker name
     */
    private String brokerName;

    /**
     * namesrv
     */
    private String namesrv;

}
