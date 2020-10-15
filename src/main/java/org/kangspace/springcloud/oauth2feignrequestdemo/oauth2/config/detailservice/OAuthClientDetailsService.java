package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice;

import lombok.extern.slf4j.Slf4j;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model.OAuthClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * 客户端信息服务
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/14 15:04
 */
@Slf4j
@Component
public class OAuthClientDetailsService implements ClientDetailsService {
    private Map<String, ClientDetails> clientDetailsStore = new HashMap();

    public OAuthClientDetailsService() {
    }

    public void setClientDetailsStore(Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap(clientDetailsStore);
    }
    @Override
    public OAuthClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OAuthClientDetails details = (OAuthClientDetails) this.clientDetailsStore.get(clientId);
        if (details == null) {
            throw new NoSuchClientException("No client with requested id: " + clientId);
            //可配置查询DB中Client信息
//            addClient(clientId,clientDetails);
        }
        log.warn("OAuthClientDetailsService.loadClientByClientId:cliendId:{},clientDetails:{}",clientId,details);
        return details;
    }

    protected void addClient(String clientId, ClientDetails value) {
        this.clientDetailsStore.put(clientId, value);
    }

}
