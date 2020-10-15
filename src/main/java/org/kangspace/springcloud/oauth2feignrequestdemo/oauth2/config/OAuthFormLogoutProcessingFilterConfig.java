package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.filter.OAuthFormLogoutProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 自定义HttpSecurity配置-用于Form登出处理过滤器的配置
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 18:10
 */
@Component
public class OAuthFormLogoutProcessingFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{
    /**
     * Form登出过滤器
     */
    @Autowired
    private OAuthFormLogoutProcessingFilter oAuthFormLogoutProcessingFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder
                //添加过滤器
                //.addFilter(oAuthFormLogoutProcessingFilter)
                //或
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer.logoutSuccessHandler(new OAuthFormLogoutProcessingFilter.OAuthLogoutSuccessHandler());
                });
    }
}
