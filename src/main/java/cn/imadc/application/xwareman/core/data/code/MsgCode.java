package cn.imadc.application.xwareman.core.data.code;

import cn.imadc.application.base.common.code.ResponseCode;

/**
 * <p>
 * 错误码定义
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
public final class MsgCode extends ResponseCode {

    /**
     * ***************************************************其它错误*****************************************************
     */
    // 未知异常
    public static final String UNKNOWN_000001 = "UNKNOWN_000001";

    /**
     * ***************************************************接口请求*****************************************************
     */
    // 未登录
    public static final String API_000001 = "API_000001";
    // 未授权
    public static final String API_000002 = "API_000002";
}
