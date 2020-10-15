package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.filter;

import lombok.extern.slf4j.Slf4j;
import org.kangspace.springcloud.oauth2feignrequestdemo.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * 用户名密码认证登出处理过滤器
 * FORM URL: /logout
 * 处理流程: AbstractAuthenticationProcessingFilter#attemptAuthentication ->
 *           getAuthenticationManager()#authenticate(authRequest)->
 *           ProviderManager#authenticate(authRequest)->
 *           AuthenticationProvider#authenticate(authRequest)->
 *
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 18:10
 */
@Slf4j
@Component
public class OAuthFormLogoutProcessingFilter extends LogoutFilter {

    public static final String OAUTH_FORM_LOGOUT_URI = "/logout";

    public OAuthFormLogoutProcessingFilter() {
        super(new OAuthLogoutSuccessHandler(), (LogoutHandler) (request, response, authentication) -> {
            //TODO logout处理
            log.warn("logout:"+authentication);
        });
    }

    /**
     * 登出处理
     */
    public static class OAuthLogoutSuccessHandler implements LogoutSuccessHandler{
        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            log.warn("logoutSuccess");
            WebUtil.writeJson(response,"{\"msg\":\"logout success\"");
        }
    }

}
