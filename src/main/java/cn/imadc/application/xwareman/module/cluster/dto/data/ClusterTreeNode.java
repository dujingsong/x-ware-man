package cn.imadc.application.xwareman.module.cluster.dto.data;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 集群吗树节点
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-05
 */
@Getter
@Setter
public class ClusterTreeNode extends BaseResponseDTO {

    /**
     * 名称
     */
    private String title;

    /**
     * 唯一标识
     */
    private Long key;

    /**
     * 子节点
     */
    private List<ClusterTreeNode> children;

}
