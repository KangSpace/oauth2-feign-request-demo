package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model;

import org.springframework.beans.BeanUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * <pre>
 * 客户端详细信息实体
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/14 15:06
 */
public class OAuthClientDetails extends BaseClientDetails {

    public OAuthClientDetails() {
    }

    public OAuthClientDetails(ClientDetails value) {
        BeanUtils.copyProperties(value,this );
    }
}
