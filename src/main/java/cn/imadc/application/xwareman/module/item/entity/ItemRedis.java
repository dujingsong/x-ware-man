package cn.imadc.application.xwareman.module.item.entity;

import cn.imadc.application.base.common.persistence.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    // Server

    /**
     * 进程ID
     */
    private Integer processId;

    /**
     * runId
     */
    private String runId;

    /**
     * 启动时间
     */
    private Long uptimeInSeconds;

    // Clients

    /**
     * 连接数
     */
    private Integer connectedClients;

    /**
     * 阻塞连接数
     */
    private Integer blockedClients;

    // Memory

    /**
     * 内存使用量b
     */
    private Long usedMemory;

    // Persistence

    /**
     * 上次RDB保存以后改变的key次数
     */
    private Long rdbChangesSinceLastSave;

    /**
     * 当前是否在进行bgsave操作。1:是 0:否
     */
    @TableField(value = "rdb_bgsave_in_progress")
    private Integer rdbBgSaveInProgress;

    /**
     * 上次rdb保存的时间
     */
    private Long rdbLastSaveTime;

    /**
     * 上次rdb保存的状态
     */
    @TableField(value = "rdb_last_bgsave_status")
    private String rdbLastBgSaveStatus;

    /**
     * 上次rdb保存的耗时
     */
    @TableField(value = "rdb_last_bgsave_time_sec")
    private Long rdbLastBgSaveTimeSec;

    /**
     * 当前rdb保存的耗时
     */
    @TableField(value = "rdb_current_bgsave_time_sec")
    private Long rdbCurrentBgSaveTimeSec;

    /**
     * rdb上一次cow大小
     */
    private Long rdbLastCowSize;

    /**
     * 是否开启了aof
     */
    private Integer aofEnabled;

    /**
     * aof是否进行中
     */
    private Integer aofRewriteInProgress;

    /**
     * rdb进行期间是否有等待的aof
     */
    private Integer aofRewriteScheduled;

    /**
     * 最近一次AOF重写操作消耗的时间
     */
    private Long aofLastRewriteTimeSec;

    /**
     * 当前正在执行的AOF操作已经消耗的时间
     */
    private Long aofCurrentRewriteTimeSec;

    /**
     * 最近一次AOF重写操作是否执行成功
     */
    @TableField(value = "aof_last_bgrewrite_status")
    private String aofLastBgRewriteStatus;

    /**
     * 最近一次追加操作是否执行成功
     */
    private String aofLastWriteStatus;

    /**
     * 在执行AOF重写期间，分配给COW的大小
     */
    private Long aofLastCowSize;

    /**
     * AOF的当前大小
     */
    private Long aofCurrentSize;

    /**
     * 最近一次重写后AOF的大小
     */
    private Long aofBaseSize;

    /**
     * 是否有AOF操作在等待执行
     */
    private Integer aofPendingRewrite;

    /**
     * AOF buffer的大小
     */
    private Long aofBufferLength;

    /**
     * AOF重写buffer的大小
     */
    private Long aofRewriteBufferLength;

    /**
     * 在等待执行的fsync操作的数量
     */
    private Integer aofPendingBioFsync;

    /**
     * Fsync操作延迟执行的次数
     */
    private Integer aofDelayedFsync;

    // Stats

    /**
     * 已接收连接数
     */
    private Long totalConnectionsReceived;

    /**
     * 已执行命令数
     */
    private Long totalCommandsProcessed;

    /**
     * 入流量/s
     */
    private Double instantaneousInputKbps;

    /**
     * 出流量/s
     */
    private Double instantaneousOutputKbps;

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

    /**
     * 拒绝连接数
     */
    private Long rejectedConnections;

    /**
     * 命中的key
     */
    private Long keyspaceHits;

    /**
     * 未命中的key
     */
    private Long keyspaceMisses;

    /**
     * 上一次fork用时
     */
    private Long latestForkUsec;

    // Replication

    /**
     * 角色
     */
    private String role;

    // Keyspace

    /**
     * 使用的db数量
     */
    private Integer usedDb;

    /**
     * db中key总数量
     */
    private Long dbKey;

    /**
     * db的过期key总数量
     */
    private Long dbExpires;
}
