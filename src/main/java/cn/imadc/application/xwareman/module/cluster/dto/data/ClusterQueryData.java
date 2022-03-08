package cn.imadc.application.xwareman.module.cluster.dto.data;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-08
 */
@Getter
@Setter
public class ClusterQueryData implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
}
