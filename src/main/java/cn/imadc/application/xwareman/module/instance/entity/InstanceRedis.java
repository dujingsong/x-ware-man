package cn.imadc.application.xwareman.module.instance.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
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
