package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model.OAuthClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 自定义客户端信息服务Builder
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/14 15:04
 */
@Component
public class OAuthCacheableInMemeryClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<OAuthCacheableInMemeryClientDetailsServiceBuilder> {
    private Map<String, ClientDetails> clientDetails = new HashMap();
    /**
     * 客户端详情服务
     */
    @Autowired
    private OAuthClientDetailsService oAuthClientDetailsService;

    @Override
    protected void addClient(String clientId, ClientDetails value) {
        this.clientDetails.put(clientId, value instanceof OAuthClientDetails?value:new OAuthClientDetails(value));
    }

    @Override
    protected ClientDetailsService performBuild() {
        oAuthClientDetailsService.setClientDetailsStore(this.clientDetails);
        return oAuthClientDetailsService;
    }
}
