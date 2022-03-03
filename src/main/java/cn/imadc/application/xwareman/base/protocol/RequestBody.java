package cn.imadc.application.xwareman.base.protocol;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-03
 */
public abstract class RequestBody implements Serializable {

    // 类别
    private String remotingType;

    // 消息
    private byte[] data;
}
