package org.kangspace.springcloud.oauth2feignrequestdemo.interceptor;

import org.kangspace.springcloud.oauth2feignrequestdemo.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/15 14:49
 */
public class TempAccessTokenCheckInterceptor implements HandlerInterceptor {
    public static final String AuthorizationHeaderName = "Authorization";
    public static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
    public static String TEMP_RESOURCE_URI = "/temp/**";
    private RequestMatcher matcher = new AntPathRequestMatcher(TEMP_RESOURCE_URI);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization= request.getHeader(AuthorizationHeaderName);
        String bearerToken = authorization != null ? authorization.split(AUTHORIZATION_BEARER_PREFIX)[1]:null;
        if (matcher.matches(request) && bearerToken == null) {
            WebUtil.writeJson(response,"{\"error\":\"authorization token is null\"}");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }
}
