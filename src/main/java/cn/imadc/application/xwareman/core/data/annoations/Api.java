package cn.imadc.application.xwareman.core.data.annoations;

import cn.imadc.application.base.common.enums.AuthType;

import java.lang.annotation.*;

/**
 * <p>
 * 接口上的注解
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface Api {

    /**
     * 鉴权类型
     *
     * @return 见枚举AuthType
     */
    AuthType authType() default AuthType.AUTHORIZED;
}
