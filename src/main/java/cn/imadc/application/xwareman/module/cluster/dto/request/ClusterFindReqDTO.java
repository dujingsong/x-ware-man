package cn.imadc.application.xwareman.module.cluster.dto.request;

import cn.imadc.application.base.common.search.BaseSearchDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 集群查询参数
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
public class ClusterFindReqDTO extends BaseSearchDTO implements Serializable {

    /**
     * 0：redis；1：rocketmq
     */
    private Integer type;
}
