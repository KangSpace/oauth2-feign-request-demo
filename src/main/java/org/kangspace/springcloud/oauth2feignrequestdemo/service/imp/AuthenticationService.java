package org.kangspace.springcloud.oauth2feignrequestdemo.service.imp;

import lombok.extern.slf4j.Slf4j;
import org.kangspace.springcloud.oauth2feignrequestdemo.feign.ThirdOAuthLoginClient;
import org.kangspace.springcloud.oauth2feignrequestdemo.service.iface.AbstractCacheableAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * 可缓存的认证服务
 * 用于维护token缓存及获取新token
 * </pre>
 * @see AuthenticationService#getAccessToken(String, String, String, String)
 * @see AbstractCacheableAuthenticationService#getAccessToken(String, String, String, String)
 * @author kango2gler@gmail.com
 * @date 2020/9/14 15:33
 */
@Slf4j
@Service
public class AuthenticationService extends AbstractCacheableAuthenticationService {
    @Autowired
    private ThirdOAuthLoginClient thirdOAuthLoginClient;

    @Override
    public String freshAccessToken(String username, String password, String appId, String appSecret) {
        return thirdOAuthLoginClient.token(username,password,appId,appSecret);
    }
}
