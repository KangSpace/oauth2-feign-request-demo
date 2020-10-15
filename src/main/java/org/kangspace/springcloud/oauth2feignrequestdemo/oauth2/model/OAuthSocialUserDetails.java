package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model;

import org.springframework.social.security.SocialUserDetails;

/**
 * <pre>
 * 自定义用户详情信息
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 17:03
 */
public class OAuthSocialUserDetails<T> extends OAuthUserDetails implements SocialUserDetails {
    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OAuthSocialUserDetails() {
    }

    public OAuthSocialUserDetails(String username, String password, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled, String userId, T detail) {
        super(username, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled,detail);
        this.userId = userId;
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
