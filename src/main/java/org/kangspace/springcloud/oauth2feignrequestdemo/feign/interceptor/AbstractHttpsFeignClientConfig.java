package org.kangspace.springcloud.oauth2feignrequestdemo.feign.interceptor;

import feign.Client;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * <pre>
 * 用于Https请求的FeignClient
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/14 14:58
 */
@Slf4j
public abstract class AbstractHttpsFeignClientConfig {
    public SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        return ctx;
    }

    public HostnameVerifier hostnameAllVerified(){
        return (hostname, session) -> true;
    }

    public Client newHttpsFeignClient() throws KeyManagementException, NoSuchAlgorithmException {
        return new Client.Default(getSSLContext().getSocketFactory(),hostnameAllVerified());
    }
}
