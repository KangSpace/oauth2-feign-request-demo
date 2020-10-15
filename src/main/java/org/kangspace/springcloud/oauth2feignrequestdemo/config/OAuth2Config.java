package org.kangspace.springcloud.oauth2feignrequestdemo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.text.MessageFormat;

/**
 * <pre>
 * OAuth2相关配置信息
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/30 16:27
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Data
public class OAuth2Config {
    private String appId;

    private String appSecret;

    private String openId;

    private String openSecret;
    /**
     * <pre>
     * 缓存Key前缀
     * 缓存key完整格式: cacheKeyPrefix[:appId][:openId]
     * </pre>
     */
    @Value("${cacheKey:oAuthCacheKey}")
    private String cacheKeyPrefix;
    /**
     * <pre>
     * 缓存时长,单位ms
     * 默认: 3小时
     * </pre>
     */
    @Value("${cacheTimeOut:#{60*60*3*1000}}")
    private Long cacheTimeOut;
    /**
     * <pre>
     * oAuth2认证操作锁前缀
     * (对于每个AppId全局维护一个Token,所以在token失效,重新获取token时,加全局分布式锁)
     * 锁key完整格式: oAuthLockKeyPrefix[:appId][:openId]
     * </pre>
     * @see org.kangspace.springcloud.oauth2feignrequestdemo.service.iface.AbstractCacheableAuthenticationService#FRESH_ACCESSTOKEN_LOCK_TIMEOUT
     * @see org.kangspace.springcloud.oauth2feignrequestdemo.service.iface.AbstractCacheableAuthenticationService#FRESH_ACCESSTOKEN_LOCK_VAL
     */
    @Value("${oAuthLockKeyPrefix:oauth2:oAuthLockKey}")
    private String oAuthLockKeyPrefix;

    /**
     * 获取具体的缓存key
     * @return
     */
    public String getCacheKey(){
        Assert.notNull(appId,"appId must be not null, please set config property [oauth2.appId]");
        Assert.notNull(openId,"appId must be not null, please set config property [oauth2.openId]");
        return MessageFormat.format(cacheKeyPrefix + ":{0}:{1}", appId, openId);
    }

    public String getOAuthLockKey() {
        Assert.notNull(appId,"appId must be not null, please set config property [oauth2.appId]");
        Assert.notNull(openId,"appId must be not null, please set config property [oauth2.openId]");
        return MessageFormat.format(oAuthLockKeyPrefix + ":{0}:{1}", appId,openId);
    }
}
