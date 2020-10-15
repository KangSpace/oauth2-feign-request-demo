package org.kangspace.springcloud.oauth2feignrequestdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Spring Cloud oAuth2 Feign Client Requests Demo Application
 */
@SpringBootApplication
@EnableFeignClients
public class OAuth2FeignRequestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2FeignRequestDemoApplication.class, args);
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
