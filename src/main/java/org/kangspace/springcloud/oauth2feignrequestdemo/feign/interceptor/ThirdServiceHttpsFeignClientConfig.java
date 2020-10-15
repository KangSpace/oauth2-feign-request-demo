package org.kangspace.springcloud.oauth2feignrequestdemo.feign.interceptor;

import feign.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * <pre>
 * 数校API服务Https Feign拦截器配置
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/14 14:58
 */
@Slf4j
public class ThirdServiceHttpsFeignClientConfig extends AbstractHttpsFeignClientConfig{
    /**
     * 当访问接口为Https时,构建Https的FeignClient
     * @param cachingFactory
     * @param clientFactory
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    @Bean
    @ConditionalOnExpression("'${third-service.gateway.url}'.startsWith(\"https\")")
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) throws NoSuchAlgorithmException, KeyManagementException {
        return new LoadBalancerFeignClient(newHttpsFeignClient(),cachingFactory, clientFactory);
    }
}
