package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.config.interceptor.AuthInterceptor;
import cn.imadc.application.xwareman.core.config.interceptor.CtxInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 添加拦截器的配置
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@AllArgsConstructor
@Configuration
public class AppWebMvcConfiguration implements WebMvcConfigurer {

    private final CtxInterceptor ctxInterceptor;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对所有的请求地址进行处理
        registry.addInterceptor(ctxInterceptor)
                .addPathPatterns("/**");
        // 对所有的请求地址进行鉴权
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**");
    }
}
