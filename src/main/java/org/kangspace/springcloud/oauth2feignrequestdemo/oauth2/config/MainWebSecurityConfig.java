package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice.OAuthUserDetailsService;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.filter.OAuthFormLoginProcessingFilter;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.filter.OAuthFormLogoutProcessingFilter;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.handler.OAuthFailureHandler;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.handler.OAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <pre>
 * 自定义WebSecurityConfigurerAdapter,并开启SpringSecurity
 * 配置身份认证组件及HttpSecurity配置
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/10 17:59
 */
@Configuration
@EnableWebSecurity
public class MainWebSecurityConfig extends WebSecurityConfigurerAdapter {
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
    /**
     * 认证处理过滤器
     */
    @Autowired
    private OAuthFormLoginProcessingFilter oAuthFormLoginProcessingFilter;
    /**
     * 认证登出处理过滤器
     */
    @Autowired
    private OAuthFormLogoutProcessingFilter oAuthFormLogoutProcessingFilter;
    /**
     * Form登录认证处理过滤器配置
     */
    @Autowired
    private OAuthFormLoginProcessingFilterConfig oAuthFormLoginProcessingFilterConfig;
    /**
     * Form登出认证处理过滤器配置
     */
    @Autowired
    private OAuthFormLogoutProcessingFilterConfig oAuthFormLogoutProcessingFilterConfig;
    /**
     * 认证成功处理器
     */
    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;
    /**
     * 认证失败处理器
     */
    @Autowired
    private OAuthFailureHandler oAuthFailHandler;

    public MainWebSecurityConfig() {
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager authenticationManager = super.authenticationManagerBean();
        return authenticationManager;
    }

    /**
     * 配置公共身份认证组件
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oAuthAuthenticationProvider)
                .userDetailsService(oAuthUserDetailsService);
    }

    public static String ERROR_URI = "/error";
    /**
     * 用于测试的临时资源URI
     */
    public static String DEMP_RESOURCE_URI = "/demo/**";
    public static String TEMP_RESOURCE_URI = "/temp/**";
    /**
     * 配置HttpSecurity
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //可添加各种过滤器
        http
                //exceptionHandling添加异常处理配置
                .addFilter(oAuthFormLoginProcessingFilter).exceptionHandling().and()
//                .addFilter(oAuthFormLogoutProcessingFilter).exceptionHandling().and()
                .apply(oAuthFormLogoutProcessingFilterConfig).and()
                //或使用Apply加入登录处理过滤器的配置
//                .apply(oAuthFormLoginProcessingFilterConfig).and()
                //添加form登录配置,此处忽略,使用我们自己的过滤器
//                .formLogin().successHandler(oAuthSuccessHandler).failureHandler(oAuthFailHandler)
                .authorizeRequests()
                .antMatchers(OAuthFormLoginProcessingFilter.OAUTH_FORM_LOGIN_URI,
                        OAuthFormLogoutProcessingFilter.OAUTH_FORM_LOGOUT_URI,
                        MainAuthorizationServerConfig.ALL_OAUTH_URI,
                        ERROR_URI,
                        DEMP_RESOURCE_URI,
                        TEMP_RESOURCE_URI
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                //禁用Http Response Header: X-Frame-Options
                .headers().frameOptions().disable()
                .and()
                //禁用CORS
                .cors().disable()
                .csrf().disable()
                .requestCache().disable();
    }
}