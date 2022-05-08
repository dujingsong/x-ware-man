package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.config.sql.MybatisIntercepts;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * mybatis plus配置
 * </p>
 *
 * @author 杜劲松
 * @since 2021-12-24
 */
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 自定义拦截器，先添加先执行。
        interceptor.addInnerInterceptor(new MybatisIntercepts());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
