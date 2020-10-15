package org.kangspace.springcloud.oauth2feignrequestdemo.feign;

import org.kangspace.springcloud.oauth2feignrequestdemo.feign.interceptor.ThirdServiceHttpsFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * 第三方认证登录服务接口
 * (此处未配置熔断实现)
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/30 17:31
 */
@FeignClient(name = "thirdOAuthLoginClient", url = "${third-service.gateway.oauth.url}",configuration = {ThirdServiceHttpsFeignClientConfig.class})
public interface ThirdOAuthLoginClient {
    /**
     * 第三方认证接口
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @return
     */
    @PostMapping("token?grant_type=password")
    String token(@RequestParam("username") String username,@RequestParam("password") String password,
                 @RequestParam("client_id") String clientId, @RequestParam("client_secret") String clientSecret);
}
