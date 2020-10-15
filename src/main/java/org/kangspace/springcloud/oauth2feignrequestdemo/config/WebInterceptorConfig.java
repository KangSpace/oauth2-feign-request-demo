package org.kangspace.springcloud.oauth2feignrequestdemo.config;

import org.kangspace.springcloud.oauth2feignrequestdemo.interceptor.TempAccessTokenCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/15 14:50
 */
@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TempAccessTokenCheckInterceptor());
    }
}
