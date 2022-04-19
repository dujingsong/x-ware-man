package cn.imadc.application.xwareman.core.data.exception.advised;

import cn.imadc.application.base.common.exception.BizException;
import cn.imadc.application.base.common.exception.NotLoginException;
import cn.imadc.application.base.common.exception.UnauthorizedException;
import cn.imadc.application.base.common.response.ResponseW;
import cn.imadc.application.xwareman.core.data.code.MsgCode;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.ServletException;

/**
 * <p>
 * 全局异常处理
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Slf4j
@Order(1)
@RestControllerAdvice
public class GlobalExceptionAdvice {


    /**
     * 托底异常异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseW handleException(Exception exception) {
        log.error("托底Exception异常", exception);
        return new ResponseW(MsgCode.UNKNOWN_000001, "系统错误");
    }

    /**
     * 托底异常异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseW handleRuntimeException(RuntimeException exception) {
        log.error("托底RuntimeException异常", exception);
        return new ResponseW(MsgCode.UNKNOWN_000001, "未知异常");
    }

    /**
     * 文件上传大小超限处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ResponseBody
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseW handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return ResponseW.error("文件上传大小超限");
    }

    /**
     * 业务异常异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public ResponseW handleBizException(BizException exception) {
        log.error("业务异常异常", exception);
        return ResponseW.error(exception.getMessage());
    }

    /**
     * 请求异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ResponseBody
    @ExceptionHandler(value = ServletException.class)
    public ResponseW handleServletException(ServletException exception) {
        log.error("请求异常", exception);
        return ResponseW.error("请求异常");
    }

    /**
     * 未登录异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ResponseBody
    @ExceptionHandler(value = NotLoginException.class)
    public ResponseW handleNotLoginException(NotLoginException exception) {
        return new ResponseW(MsgCode.API_000001, "未登录");
    }

    /**
     * 未授权异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResponseW handleException(UnauthorizedException exception) {
        return new ResponseW(MsgCode.API_000002, "没有权限");
    }

    /**
     * 解析令牌异常处理
     *
     * @param exception 异常数据
     * @return JSON格式错误响应数据
     */
    @ExceptionHandler(JWTDecodeException.class)
    @ResponseBody
    public ResponseW handleException(JWTDecodeException exception) {
        return new ResponseW(MsgCode.API_000001, "token错误");
    }

}
