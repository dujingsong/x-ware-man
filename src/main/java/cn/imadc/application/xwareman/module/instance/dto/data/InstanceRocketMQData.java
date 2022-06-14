package cn.imadc.application.xwareman.module.instance.dto.data;

import cn.imadc.application.xwareman.module.instance.entity.InstanceRocketmq;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * rocketmq实例信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-06-01
 */
@Getter
@Setter
public class InstanceRocketMQData extends InstanceRocketmq implements Serializable {

    /**
     * 实例ip
     */
    private String ip;

}
