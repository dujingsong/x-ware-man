package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * redis节点发现
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Getter
@Setter
public class DiscoveryRedisReqDTO extends BaseSearchDTO {

    /**
     * 哨兵节点IP
     */
    private String sentinelIp;

    /**
     * 哨兵节点端口
     */
    private Integer sentinelPort;

    /**
     * 哨兵节点密码
     */
    private String sentinelPassword;

    /**
     * 数据节点密码
     */
    private String nodePassword;
}
