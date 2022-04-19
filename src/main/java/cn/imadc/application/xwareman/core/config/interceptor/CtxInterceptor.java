package cn.imadc.application.xwareman.core.config.interceptor;

import cn.imadc.application.base.auth.jwt.JWT;
import cn.imadc.application.base.common.context.ReqCtxConstant;
import cn.imadc.application.base.common.context.RequestContext;
import cn.imadc.application.xwareman.core.data.annoations.Api;
import cn.imadc.application.xwareman.core.data.constant.Constant;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 会话信息拦截器
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Component
public class CtxInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        RequestContext requestContext = RequestContext.getCurrentContext();

        String token = request.getHeader(Constant.ACCESS_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            requestContext.put(ReqCtxConstant.TOKEN, token);

            DecodedJWT decodedJWT = JWT.decodedJWT(token);
            requestContext.put(ReqCtxConstant.ID, Long.parseLong(decodedJWT.getSubject()));
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Api api = handlerMethod.getMethodAnnotation(Api.class);
        if (null != api) {
            requestContext.put(ReqCtxConstant.API, api);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestContext.getCurrentContext().release();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
