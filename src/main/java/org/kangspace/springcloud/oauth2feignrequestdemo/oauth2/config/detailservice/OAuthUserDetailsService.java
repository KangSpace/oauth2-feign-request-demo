package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model.OAuthSocialUserDetails;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model.OAuthUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <pre>
 * 用户详情服务,用于通过用户名获取用户信息
 * UserDetailsService: 基本用户信息服务,提供使用用户名密码获取用户信息
 * UserDetailsService: 基本用户信息服务,提供使用用户名密码获取用户信息
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 17:02
 */
@Component
public class OAuthUserDetailsService implements UserDetailsService, SocialUserDetailsService {
    private final String DEFAULT_SUCCESS_USER_NAME = "success";
    private final String DEFAULT_FAIL_USER_NAME = "fail";
    private final List<String> DEFAULT_USER_NAMES = CollectionUtils.arrayToList(new String[]{DEFAULT_SUCCESS_USER_NAME, DEFAULT_FAIL_USER_NAME});

    @Override
    public OAuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!DEFAULT_USER_NAMES.contains(username)) {
            throw new UsernameNotFoundException("username: "+ username+" not found, support username in :"+DEFAULT_USER_NAMES);
        }
        if (DEFAULT_SUCCESS_USER_NAME.equals(username)) {
            return new OAuthUserDetails(username,username,true,true,true,true,username);
        }
        return new OAuthUserDetails(username,"",false,false,false,true,null);
    }

    @Override
    public OAuthSocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        return new OAuthSocialUserDetails(null,"",true,true,true,true,userId,userId);
    }
}
