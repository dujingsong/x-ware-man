package cn.imadc.application.xwareman.core.config;

import cn.imadc.application.xwareman.core.data.client.RocketMQClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * RocketMQ通信客户端配置
 * </p>
 *
 * @author 杜劲松
 * @since 2022-03-07
 */
@Configuration
public class RocketMQClientConfiguration {

    @Bean
    public RocketMQClient rocketMQClient() {
        return new RocketMQClient();
    }
}
