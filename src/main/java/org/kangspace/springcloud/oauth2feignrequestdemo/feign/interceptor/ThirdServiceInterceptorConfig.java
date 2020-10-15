package org.kangspace.springcloud.oauth2feignrequestdemo.feign.interceptor;

import feign.RequestInterceptor;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.kangspace.springcloud.oauth2feignrequestdemo.config.OAuth2Config;
import org.kangspace.springcloud.oauth2feignrequestdemo.service.imp.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 第三方API服务Feign拦截器配置
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/14 14:58
 */
@Slf4j
public class ThirdServiceInterceptorConfig extends ThirdServiceHttpsFeignClientConfig {
    public static final String AuthorizationHeaderName = "Authorization";
    public static final String AUTHORIZATION_BEARER_PREFIX = "Bearer ";
    /**
     * <pre>
     * 获取AccessToken的线程池
     * 由于hystrix.command.default.execution.isolation.strategy默认为Thread,导致一个线程中,在Retryer重试获取AccessToken时抛出
     * command executed multiple times - this is not permitted.及 java.lang.IllegalStateException: This instance can only be executed once. Please instantiate a new instance.
     * 异常,所以开启新线程获取AccessToken.
     * 不使用新线程请求AccessToken会发生的情况:
     * 一般情况下,AccessToken获取后会缓存一段时间,只有在重新获取AccessToken(401)时,才会出现Retryer重试异常
     * </pre>
     */
    private static final ExecutorService AUTHORIZATION_EXEC = new ThreadPoolExecutor(10, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10),
            new DefaultThreadFactory("third-authorization-pool", false));
    /**
     * 重新获取AccessToken的ThreadLocal
     */
    public static final ThreadLocal<Boolean> RETRY_GET_ACCESSTOKEN_THREADLOADL = new ThreadLocal<Boolean>();
    /**
     * 401异常后100ms后重试
     */
    public static final long RETRY_GET_ACCESSTOKEN_AFTERTIME_ON_401 = 100L;

    private ErrorDecoder errorDecoder = new ErrorDecoder.Default();

    @Autowired
    private OAuth2Config oAuth2Config;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 自定义重试机制
     *
     * @return CustomFeignRetryer
     */
    @Bean
    public Retryer feignRetryer() {
        //最大请求次数为5，初始间隔时间为100ms，下次间隔时间1.5倍递增，重试间最大间隔时间为1s，
        return new CustomFeignRetryer();
    }

    /**
     * 自定义拦截器,Header设置Authorization Bearer token
     *
     * @return
     */
    @Bean
    public RequestInterceptor interceptor() {
        return requestTemplate -> {
            boolean isRefresh = false;
            if (requestTemplate.headers().containsKey(AuthorizationHeaderName)) {
                requestTemplate.header(AuthorizationHeaderName, new String[]{null});
                //清除重试状态
                RETRY_GET_ACCESSTOKEN_THREADLOADL.remove();
                isRefresh = true;
            }
            boolean finalIsRefresh = isRefresh;
            String token = null;
            try {
                token = AUTHORIZATION_EXEC.submit(() -> authenticationService.getAccessToken(
                        oAuth2Config.getOpenId(),
                        oAuth2Config.getOpenSecret(),
                        oAuth2Config.getAppId(),
                        oAuth2Config.getAppSecret(),
                        finalIsRefresh)).get();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            //设置请求头
            requestTemplate.header(AuthorizationHeaderName, AUTHORIZATION_BEARER_PREFIX + token);
        };
    }

    /**
     * 自定义错误处理
     */
    @Bean
    public ErrorDecoder feignError() {
        return (methodKey, response) -> {
            int code = response.status();
            String body = response.body().toString();
            //401,AccessToken失效重试
            if (code == HttpStatus.UNAUTHORIZED.value()) {
                //AccessToken失效时,可再次重试
                //设置重试,重新获取AccessToken
                RETRY_GET_ACCESSTOKEN_THREADLOADL.set(Boolean.TRUE);
                log.debug("feignError need re-login to get accessToken,key={}", methodKey);
                throw new RetryableException(code, code + ":" + body, response.request().httpMethod(),
                        new RuntimeException("Request Error,code:401"),
                        new Date(System.currentTimeMillis() + RETRY_GET_ACCESSTOKEN_AFTERTIME_ON_401),
                        response.request());
            } else {
                log.error("请求{}服务{}参数错误,返回:{}", methodKey, code, body);
            }
            // 其他异常交给Default去解码处理
            // 这里使用单例即可，Default不用每次都去new
            return errorDecoder.decode(methodKey, response);
        };
    }

    /**
     * 自定义Feign重试器
     *
     * @see Retryer.Default
     */
    public static class CustomFeignRetryer extends Retryer.Default {
        @Override
        public void continueOrPropagate(RetryableException e) {
            log.debug("feignRetryer#continueOrPropagate", e);
            if (!RETRY_GET_ACCESSTOKEN_THREADLOADL.get()) {
                super.continueOrPropagate(e);
            }
            log.debug("feignError re-login to get accessToken");
        }

        /**
         * 需重写clone方法,否则会调用{@link Default#clone()} 方法
         *
         * @return
         * @see Default#clone()
         * @see feign.SynchronousMethodHandler#invoke(Object[])
         */
        @Override
        public Retryer clone() {
            return new CustomFeignRetryer();
        }
    }
}
