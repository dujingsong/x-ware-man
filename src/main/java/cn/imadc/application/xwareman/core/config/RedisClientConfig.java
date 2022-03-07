package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.base.lettuce.RedisClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Configuration
public class RedisClientConfig {

    @Bean
    public RedisClient redisClient() {
        return new RedisClient();
    }
}
