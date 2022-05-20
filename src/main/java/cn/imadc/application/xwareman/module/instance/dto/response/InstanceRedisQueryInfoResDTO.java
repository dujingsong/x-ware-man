package cn.imadc.application.xwareman.module.instance.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import cn.imadc.application.base.data.structure.redis.RedisInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 查询redis实例响应参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-20
 */
@Getter
@Setter
public class InstanceRedisQueryInfoResDTO extends BaseResponseDTO implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 进程id
     */
    private Integer pid;

    /**
     * os
     */
    private String os;

    /**
     * 内存信息
     */
    private String memoryDesc;

    /**
     * 备注信息
     */
    private String notes;

    /**
     * 类型
     */
    private Integer nodeType;

    /**
     * 版本信息
     */
    private String version;

    /**
     * 启动时间
     */
    private String startedTime;

    /**
     * 当前使用内存
     */
    private String currentMemory;

    /**
     * redis整体占系统内存
     */
    private String currentMemoryRss;

    /**
     * 峰值使用内存
     */
    private String peakMemory;

    /**
     * lua使用内存
     */
    private String luaMemory;

    /**
     * 当前连接数
     */
    private Integer currentConnection;

    /**
     * 累计连接数
     */
    private Long totalConnection;

    /**
     * 已执行命令数
     */
    private Long totalCommand;

    /**
     * 阻塞连接数
     */
    private Integer blockedConnection;

    /**
     * 库中键信息
     */
    private List<RedisInfo.Keyspace.DBInfo> dbInfoList;

    /**
     * 全部的信息
     */
    private Map<String, Map<String, String>> allInfo;
}
