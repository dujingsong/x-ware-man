package cn.imadc.application.xwareman.module.instance.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import cn.imadc.application.xwareman.module.instance.dto.data.DiscoveryRedisRegisterData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * redis节点注册请求参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
public class InstanceRedisRegisterReqDTO extends BaseSearchDTO {

    /**
     * 哨兵节点密码
     */
    private String sentinelPassword;

    /**
     * 数据节点密码
     */
    private String nodePassword;

    /**
     * 发现的redis节点信息
     */
    private List<DiscoveryRedisRegisterData> registerNodeData;

}
