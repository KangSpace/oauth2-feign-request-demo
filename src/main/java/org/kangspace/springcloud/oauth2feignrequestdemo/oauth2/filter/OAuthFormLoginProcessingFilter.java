package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.filter;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.handler.OAuthFailureHandler;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.handler.OAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 用户名密码认证处理过滤器
 * FORM URL: /login
 * 处理流程: AbstractAuthenticationProcessingFilter#attemptAuthentication ->
 *           getAuthenticationManager()#authenticate(authRequest)->
 *           ProviderManager#authenticate(authRequest)->
 *           AuthenticationProvider#authenticate(authRequest)+
 *           AuthenticationProvider#retrieveUser -> UserDetailsService#loadUserByUsername()->
 *           authenticated
 *
 *
 *
 *
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 18:10
 */
@Component
public class OAuthFormLoginProcessingFilter extends UsernamePasswordAuthenticationFilter {

    public static final String OAUTH_FORM_LOGIN_URI = "/login";

    @Autowired
    private AuthenticationManager authenticationManagerBean;
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


    @Override
    public void afterPropertiesSet() {
        //需设置AuthenticationManager ,或使用new OAuthFormLoginProcessingFilter() setAuthenticationManager
        setAuthenticationManager(authenticationManagerBean);
        super.afterPropertiesSet();
        //需单独设置AuthenticationSuccessHandler,默认为:SavedRequestAwareAuthenticationSuccessHandler
        super.setAuthenticationSuccessHandler(oAuthSuccessHandler);
        //默认为:SimpleUrlAuthenticationFailureHandler
        super.setAuthenticationFailureHandler(oAuthFailHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }
}
