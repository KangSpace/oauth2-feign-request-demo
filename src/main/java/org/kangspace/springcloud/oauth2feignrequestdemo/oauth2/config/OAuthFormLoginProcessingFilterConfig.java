package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice.OAuthUserDetailsService;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.filter.OAuthFormLoginProcessingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 自定义HttpSecurity配置-用于Form登录处理过滤器的配置
 * 可配置AuthenticationManager,AuthenticationProvider, Filter,AuthenticationSuccessHandler,AuthenticationFailureHandler等
 * 可在{@link MainWebSecurityConfig#configure(HttpSecurity)}中直接apply(OAuthFormLoginProcessingFilterConfig)
 * 或在{@link MainWebSecurityConfig#configure(HttpSecurity)}中addFilter,选其一即可
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 18:10
 */
@Component
public class OAuthFormLoginProcessingFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {
    /**
     * Form登录过滤器
     */
    @Autowired
    private OAuthFormLoginProcessingFilter oAuthFormLoginProcessingFilter;
    /**
     * 认证提供者,提供具体认证处理
     */
    @Autowired
    private OAuthAuthenticationProvider oAuthAuthenticationProvider;
    /**
     * 用户信息服务,用户通过用户名获取用户信息
     */
    @Autowired
    private OAuthUserDetailsService oAuthUserDetailsService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilter(oAuthFormLoginProcessingFilter)
                .authenticationProvider(oAuthAuthenticationProvider);
        super.configure(builder);
    }

}
