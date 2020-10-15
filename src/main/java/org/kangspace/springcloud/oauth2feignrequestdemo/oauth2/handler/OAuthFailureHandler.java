package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.handler;

import org.kangspace.springcloud.oauth2feignrequestdemo.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * OAuth认证失败以后处理器
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 17:42
 */
@Component
public class OAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        HttpStatus status= HttpStatus.UNAUTHORIZED;
        response.setStatus(status.value());
        exception.setStackTrace(new StackTraceElement[0]);
        //返回错误信息
        WebUtil.writeJson(response,new ResponseEntity<Exception>(exception, status));
    }
}
