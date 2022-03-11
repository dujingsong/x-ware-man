package cn.imadc.application.xwareman.module.item.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * redis监控项数据搜集表
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
@TableName("item_redis")
public class ItemRedis extends BaseEntity implements Serializable {

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
     * 实例redisID
     */
    private Long instanceRedisId;

    /**
     * 内存使用量b
     */
    private Long usedMemory;

    /**
     * 连接数
     */
    private Integer connectedClients;

    /**
     * 阻塞连接数
     */
    private Integer blockedClients;

    /**
     * 已执行命令数
     */
    private Long totalCommandsProcessed;

    /**
     * 入流量/s
     */
    private Long instantaneousInputKbps;

    /**
     * 出流量/s
     */
    private Long instantaneousOutputKbps;

    /**
     * 过期的Key数量
     */
    private Long expiredKeys;

    /**
     * 剔除的key数量
     */
    private Long evictedKeys;

    /**
     * 每秒操作数
     */
    private Long instantaneousOpsPerSec;
}
