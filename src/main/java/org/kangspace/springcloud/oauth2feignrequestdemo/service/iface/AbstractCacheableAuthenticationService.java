package org.kangspace.springcloud.oauth2feignrequestdemo.service.iface;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kangspace.springcloud.oauth2feignrequestdemo.config.OAuth2Config;
import org.kangspace.springcloud.oauth2feignrequestdemo.config.SpringApplicationContext;
import org.kangspace.springcloud.oauth2feignrequestdemo.exception.AccessTokenNoPermissionException;
import org.springframework.data.redis.core.BoundKeyOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 可缓存的认证服务
 * 用于维护token缓存及获取新token
 * 必须实现 {@link AbstractCacheableAuthenticationService#freshAccessToken(String, String, String, String)}方法
 * 依赖于 {@link OAuth2Config} Bean
 * 依赖于 {@link RedisTemplate redisTemplate} Bean(ByName) 需换为redission
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/14 15:33
 * @see AbstractCacheableAuthenticationService#getAccessToken(String, String, String, String) 获取缓存accessToken
 * @see AbstractCacheableAuthenticationService#freshAccessToken(String, String, String, String) 获取新的accessToken
 * @see AbstractCacheableAuthenticationService#basicToken(String, String)
 */
@Slf4j
public abstract class AbstractCacheableAuthenticationService {

    private final String FRESH_ACCESSTOKEN_LOCK_VAL = "1";
    /**
     * 检查刷新AccessToken锁超时时间
     */
    private final long FRESH_ACCESSTOKEN_LOCK_RECHECK_TIMEOUT = 100_000L;
    /**
     * <pre>
     * AccessToken锁超时时间: 10s
     * </pre>
     */
    private final long FRESH_ACCESSTOKEN_LOCK_TIMEOUT = 10_000L;

    private final String JSON_STRING_PREFIX = "{";

    /**
     * <pre>
     * 获取新的AccessToken(无缓存)
     * </pre>
     *
     * @param username
     * @param password
     * @param appId
     * @param appSecret
     * @return
     */
    public abstract String freshAccessToken(String username, String password, String appId, String appSecret);

    /**
     * <pre>
     * 获取已缓存的AccessToken
     * 获取新token后,token缓存 {@link org.kangspace.springcloud.oauth2feignrequestdemo.config.OAuth2Config#cacheTimeOut}ms
     * 获取新token时,设置10s过期的全局分布式Redis锁(需要用到RedisTemplate Bean),存在以下2中情况:
     * 1.获取锁失败时,最多等待10s后请求新数据
     * 2.获取锁成功时,若等待过锁,则从缓存中再次获取数据,若缓存数据为空,则继续请求新数据
     * </pre>
     *
     * @param username
     * @param password
     * @param appId
     * @param appSecret
     * @param isRefresh 是否刷新缓存
     * @return accessToken
     */
    public String getAccessToken(String username, String password, String appId, String appSecret, boolean isRefresh) {
        RedisTemplate redisTemplate = SpringApplicationContext.getBean("redisTemplate", RedisTemplate.class);
        OAuth2Config oAuth2Config = SpringApplicationContext.getBean(OAuth2Config.class);
        String key = oAuth2Config.getCacheKey();
        String accessTokenStr = (String) redisTemplate.boundValueOps(key).get();
        if (!StringUtils.isEmpty(accessTokenStr) && !isRefresh) {
            log.debug("getAccessToken(appId:{} ,appSecret:{} ,username:{} ,password:{}): <<<found cache>>>:{}",appId,appSecret,username,password,accessTokenStr);
            return accessTokenStr;
        }

        String lockKey = oAuth2Config.getOAuthLockKey();
        BoundKeyOperations lock = redisTemplate.boundValueOps(lockKey);
        int waitCount = 0;
        //获取不到锁时,最多等待10s
        String accessToken;
        try {
            //TODO 需换RedLock
            while (!((BoundValueOperations) lock).setIfAbsent(FRESH_ACCESSTOKEN_LOCK_VAL)) {
                log.debug("getAccessToken(appId:{} ,appSecret:{} ,username:{} ,password:{}): <<<not get the lock>>> ,waiting {} time(s)",appId,appSecret,username,password,waitCount+1);
                if (waitCount >= 10) {
                    break;
                }
                try {
                    Thread.sleep(FRESH_ACCESSTOKEN_LOCK_RECHECK_TIMEOUT);
                } catch (InterruptedException e) {}
                waitCount++;
            }
            //token申请时,做10s分布式锁
            lock.expire(FRESH_ACCESSTOKEN_LOCK_TIMEOUT, TimeUnit.MILLISECONDS);

            //等待后拿到锁时,重新获取缓存,缓存不存在时继续获取数据
            if (waitCount > 0) {
                accessTokenStr = (String) redisTemplate.boundValueOps(key).get();
                if (!StringUtils.isEmpty(accessTokenStr)) {
                    log.debug("getAccessToken(appId:{} ,appSecret:{} ,username:{} ,password:{}): <<<re-got the lock ,and found cache>>>:{}",appId,appSecret,username,password,accessTokenStr);
                    return accessTokenStr;
                }
            }

            accessToken = freshAccessToken(username, password, appId, appSecret);
            if (accessToken != null && accessToken.startsWith(JSON_STRING_PREFIX)) {
                AccessTokenDetail accessTokenDetail = JSONObject.parseObject(accessToken, AccessTokenDetail.class);
                if (accessTokenDetail != null && !StringUtils.isEmpty(accessTokenDetail.getAccess_token())) {
                    accessTokenStr = accessTokenDetail.getAccess_token();
                    redisTemplate.boundValueOps(key).set(accessTokenStr, oAuth2Config.getCacheTimeOut(), TimeUnit.MILLISECONDS);
                    log.debug("getAccessToken(appId:{} ,appSecret:{} ,username:{} ,password:{}): <<<got the lock ,and refreshed cache>>>:{}",appId,appSecret,username,password,accessTokenStr);
                    return accessTokenStr;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw e;
        } finally {
            redisTemplate.delete(lockKey);
        }
        throw new AccessTokenNoPermissionException(appId, appSecret, username, password, accessToken);
    }

    /**
     * <pre>
     * 获取已缓存的AccessToken
     * 获取新token后,token缓存 {@link org.kangspace.springcloud.oauth2feignrequestdemo.config.OAuth2Config#cacheTimeOut}ms
     * </pre>
     *
     * @param username
     * @param password
     * @param appId
     * @param appSecret
     * @return accessToken
     * @see #getAccessToken(String, String, String, String, boolean)
     */
    public String getAccessToken(String username, String password, String appId, String appSecret) {
        return getAccessToken(username, password, appId, appSecret, false);
    }

    /**
     * 获取BasicTokenHeader 字符串
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public String basicTokenHeader(String appId, String appSecret) {
        return "Basic " + basicToken(appId, appSecret);
    }

    /**
     * 获取BasicTokenHeader
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public String basicToken(String appId, String appSecret) {
        return Base64.getEncoder().encodeToString(new StringBuilder(appId).append(":").append(appSecret).toString().getBytes());
    }


    /**
     * <pre>
     * AccessToken详情
     * </pre>
     *
     * @author kango2gler@gmail.com
     * @date 2020/9/14 16:22
     */
    @Data
    public static class AccessTokenDetail {
        private String access_token;
        private String expiresIn;
        private String refresh_token;
        private long expiration;
    }
}
