package cn.imadc.application.xwareman.module.instance.dto.response;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * <p>
 * 查询rocketmq实例响应参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-31
 */
@Getter
@Setter
public class InstanceRocketMQQueryInfoResDTO extends BaseResponseDTO implements Serializable {

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
     * 备注信息
     */
    private String notes;

    /**
     * 类型
     */
    private Integer nodeType;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * broker名称
     */
    private String brokerName;

    /**
     * broker运行时参数
     */
    private TreeMap<String, String> brokerRuntimeStats;

    /**
     * broker配置
     */
    private TreeMap<Object, Object> brokerConfig;
}
