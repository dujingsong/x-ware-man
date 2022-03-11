package cn.imadc.application.xwareman.module.instance.dto.data;

import cn.imadc.application.xwareman.module.instance.entity.InstanceRedis;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * redis实例信息
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-11
 */
@Getter
@Setter
public class InstanceRedisData extends InstanceRedis implements Serializable {

    /**
     * 实例ip
     */
    private String ip;

}
