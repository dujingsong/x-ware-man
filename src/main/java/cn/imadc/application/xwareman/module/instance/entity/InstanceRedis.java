package cn.imadc.application.xwareman.module.instance.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import cn.imadc.application.xwareman.core.util.MixAllUtil;
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
 * redis实例表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
@TableName("instance_redis")
public class InstanceRedis extends BaseEntity implements Serializable {

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
     * 区域信息
     */
    @TableField(exist = false)
    private Zone zone;

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
     * 0：主节点；1：从节点；2：哨兵节点
     */
    private Integer type;

    /**
     * master name
     */
    private String masterName;

    /**
     * 节点密码
     */
    private String password;

    /**
     * 最大内存b
     */
    private Long maxMemory;

    /**
     * 最大内存
     */
    @TableField(exist = false)
    private String maxMemoryDesc;

    public String getMaxMemoryDesc() {
        return MixAllUtil.prettyMemory(maxMemory);
    }

    /**
     * 版本信息
     */
    private String redisVersion;

    /**
     * 进程ID
     */
    private Integer processId;

    /**
     * run_id
     */
    private String runId;

    /**
     * 启动时间（秒）
     */
    private Long uptimeInSeconds;

    /**
     * 启动时间
     */
    @TableField(exist = false)
    private String uptimeInSecondsDesc;

    public String getUptimeInSecondsDesc() {
        return MixAllUtil.prettyTime(uptimeInSeconds);
    }

    /**
     * hz
     */
    private Integer hz;

    /**
     * 启动路径
     */
    private String executable;

    /**
     * 配置文件所在路径
     */
    private String configFile;

}
