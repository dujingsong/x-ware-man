package cn.imadc.application.xwareman.module.instance.dto.data;

import cn.imadc.application.base.data.structure.RedisNode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 通用redis节点信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Getter
@Setter
public class CommonRedisInfoData implements Serializable {

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 节点类型
     */
    private RedisNode type;
}
