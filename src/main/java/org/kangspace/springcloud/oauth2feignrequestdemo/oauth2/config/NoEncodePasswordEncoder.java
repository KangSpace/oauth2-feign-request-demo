package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 无加密处理的密码加密器
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/14 10:52
 */
@Component
public class NoEncodePasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }
}
