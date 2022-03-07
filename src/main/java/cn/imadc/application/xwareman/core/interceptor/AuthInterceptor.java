package cn.imadc.application.xwareman.core.interceptor;

import cn.imadc.application.base.common.context.ReqCtxConstant;
import cn.imadc.application.base.common.context.RequestContext;
import cn.imadc.application.base.common.enums.AuthType;
import cn.imadc.application.base.common.exception.NotLoginException;
import cn.imadc.application.base.common.exception.UnauthorizedException;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 鉴权拦截器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;

        // 当前会话信息
        RequestContext currentContext = RequestContext.getCurrentContext();

        Api api = currentContext.get(ReqCtxConstant.API, Api.class);
        if (null != api && api.authType().equals(AuthType.ANONYMOUS)) {
            return true;
        }

        // 登录与否
        boolean notLogin = !currentContext.containsKey(ReqCtxConstant.ID);
        if (notLogin) throw new NotLoginException();

        // 鉴权
        boolean noPermission = !currentContext.containsKey(ReqCtxConstant.ID);
        if (noPermission) throw new UnauthorizedException();

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
