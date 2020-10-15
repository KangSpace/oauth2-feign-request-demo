package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice.OAuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 自定义认证提供者,获取用户信息,认证用户信息
 * AuthenticationManager中指定AuthenticationProvider 由AuthenticationProvider进行认证
 * 在AuthenticationProvider 中使用UserDetailsService获取对应的用户信息
 * 需实现 {@link org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider}
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 16:58
 */
@Component
public class OAuthAuthenticationProvider extends DaoAuthenticationProvider {
    protected boolean hideUserNotFoundExceptions = false;

    @Autowired
    private OAuthUserDetailsService oAuthUserDetailsService;
    @Autowired
    private NoEncodePasswordEncoder  noEncodePasswordEncoder;

    @Override
    protected void doAfterPropertiesSet() {
        setUserDetailsService(oAuthUserDetailsService);
        //自定义密码加密器,此处不做加密
        setPasswordEncoder(noEncodePasswordEncoder);
    }

    public OAuthAuthenticationProvider() {
        super();
        super.setHideUserNotFoundExceptions(hideUserNotFoundExceptions);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
            //通过 retrieveUser获取到的用户信息,再次校验用户信息
//        String password = (String) usernamePasswordAuthenticationToken.getCredentials();
//        String username = (String) usernamePasswordAuthenticationToken.getPrincipal();
//        if (!username.equals(password)) {
//            throw new BadCredentialsException("username and password error, input info: username:"+username+",password:"+password);
//        }

        //此处使用DaoAuthenticationProvider的实现
        usernamePasswordAuthenticationToken.setDetails(userDetails);
        super.additionalAuthenticationChecks(userDetails, usernamePasswordAuthenticationToken);
    }
}
