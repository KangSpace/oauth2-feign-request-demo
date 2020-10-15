/**
 * <pre>
 * Simple oAuth2 implements: 认证中心实现包:用于生成token,验证token
 * 一、配置授权/认证中心@EnableAuthorizationServer
 * AuthorizationServerConfigurerAdapter 基于 WebSecurityConfigurerAdapter(@EnableWebSecurity)
 * 二、实现认证的2重方式
 * 1. 自定义OAuthFormLoginProcessingFilterConfig实现认证  /login handler,/logout handler
 * {@link org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.MainWebSecurityConfig#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)}
 * {@link org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.OAuthFormLoginProcessingFilterConfig}
 *  处理流程: WebSecurityConfigurerAdapter#configure(HttpSecurity http)(@EnableWebSecurity) 开启WebSecurity->
 *            (SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>#configure(HttpSecurity builder)配置SecurityConfigurer或AbstractAuthenticationProcessingFilter)
 *            AbstractAuthenticationProcessingFilter#attemptAuthentication (Filter需设置AuthenticationManager)->
 *            getAuthenticationManager()#authenticate(authRequest)->
 *            ProviderManager#authenticate(authRequest)(ProviderManager为默认AuthenticationManager)->
 *            AuthenticationProvider#authenticate(authRequest)+
 *            AuthenticationProvider#retrieveUser -> UserDetailsService#loadUserByUsername()->
 *            authenticated
 * 2. 使用Spring Security 框架集成的oAuth2.0认证
 *    /oauth/token({@link org.springframework.security.oauth2.provider.endpoint.TokenEndpoint})
 *    /oauth/authorize ({@link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint}) http://localhost:8086/oauth/authorize?response_type=code&client_id=clientId1&client_secret=clientSecret1&username=success&password=success
 * {@link org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.OAuthFormLogoutProcessingFilterConfig}
 * </pre>
 */
package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2;