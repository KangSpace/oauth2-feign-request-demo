package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.handler;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model.OAuthUserDetails;
import org.kangspace.springcloud.oauth2feignrequestdemo.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * OAuth认证成功以后处理器
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 17:42
 */
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //Response write or redirect or forward
        OAuthUserDetails oAuthUserDetails  = (OAuthUserDetails) authentication.getDetails();
        //TODO 生成JWT
//        OAuth2AccessToken
        WebUtil.writeJson(response,new ResponseEntity<OAuthUserDetails>(oAuthUserDetails, HttpStatus.OK));
    }
}
