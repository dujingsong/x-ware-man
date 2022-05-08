package cn.imadc.application.xwareman.module.zone.dto.data;

import cn.imadc.application.base.common.response.BaseResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 实例区域树节点
 * </p>
 *
 * @author 杜劲松
 * @since 2022-05-05
 */
@Getter
@Setter
public class ZoneTreeNode extends BaseResponseDTO {

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
    private List<ZoneTreeNode> children;

}
