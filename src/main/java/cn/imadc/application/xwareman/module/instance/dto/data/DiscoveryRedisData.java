package cn.imadc.application.xwareman.module.instance.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * redis节点发现信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Getter
@Setter
public class DiscoveryRedisData implements Serializable {

    /**
     * master name
     */
    private String masterName;

    /**
     * 哨兵节点
     */
    private List<CommonRedisInfoData> sentinels;

    /**
     * 数据节点
     */
    private List<CommonRedisInfoData> dataNodes;
}
