package org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config;

import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.config.detailservice.OAuthCacheableInMemeryClientDetailsServiceBuilder;
import org.kangspace.springcloud.oauth2feignrequestdemo.oauth2.model.OAuthUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.*;

/**
 * <pre>
 * 开启配置认证中心
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/14 14:48
 */
@Configuration
@EnableAuthorizationServer
public class MainAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    /**
     * access_token 有效期时长2小时
     */
    private static final int ACCESS_TOKEN_VALID_SECONDS = 7200;
    /**
     * refresh_token 有效时长2小时
     */
    private static final int REFRESH_TOKEN_VALID_SECONDS = 7200;

    /**
     * refresh_token 有效时长2小时
     */
    private static final String[] REDIRECT_URL = {"https://kangspace.org"};
    /**
     * 默认Scops
     */
    private static final String[] DEFALT_SCOPS = {"ALL", "WEB", "APP", "THIRD"};
    /**
     * 默认授权类型
     */
    private static final String[] DEFALT_GRANT_TYPES = {"refresh_token", "password", "authorization_code", "client_credentials", "implicit"};

    public static final String ALL_OAUTH_URI = "/oauth/**";

    /**
     * 默认JWT token签名key
     */
    public static final String DETAULT_JWT_TOKEN_KEY = "2020-10-14";

    /**
     * 默认支持的客户端列表
     */
    public static Map<String, String> DEFAULT_CLIENT_MAP = new HashMap<>();

    static {
        DEFAULT_CLIENT_MAP.put("clientId1", "clientSecret1");
        DEFAULT_CLIENT_MAP.put("clientId2", "clientSecret2");
    }


    @Autowired
    private OAuthCacheableInMemeryClientDetailsServiceBuilder oAuthCacheableInMemeryClientDetailsServiceBuilder;
    @Autowired
    private AuthenticationManager authenticationManagerBean;

    public MainAuthorizationServerConfig() {
        super();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        security
                //开启oauth/token认证:ClientCredentialsTokenEndpointFilter
                .allowFormAuthenticationForClients()
                .checkTokenAccess("permitAll()");
    }

    /**
     * 配置客户端信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.setBuilder(oAuthCacheableInMemeryClientDetailsServiceBuilder);
        DEFAULT_CLIENT_MAP.forEach((cli, csec) -> {
            //注册客户端信息
            oAuthCacheableInMemeryClientDetailsServiceBuilder.withClient(cli).secret(csec)
                    .scopes(DEFALT_SCOPS)
                    .authorizedGrantTypes(DEFALT_GRANT_TYPES)
                    //认证成功redirect_url 注册
                    .redirectUris(REDIRECT_URL)
                    .accessTokenValiditySeconds(ACCESS_TOKEN_VALID_SECONDS)
                    .refreshTokenValiditySeconds(REFRESH_TOKEN_VALID_SECONDS);
        });
        oAuthCacheableInMemeryClientDetailsServiceBuilder.build();
    }

    /**
     * JWT Token转化
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(DETAULT_JWT_TOKEN_KEY);
        return accessTokenConverter;
    }

    /**
     * 配置端点相关配置信息
     * TODO,通过自定义配置实现认证中心业务逻辑
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManagerBean);
        //配置accessToken转换器为jwt转换器
        endpoints.accessTokenConverter(jwtAccessTokenConverter());
        //token内容扩展,需要组成TokenEnhancerChain
        List<TokenEnhancer> chanChildList = new ArrayList<>(2);
        chanChildList.add(customTokenEnhancer());
        chanChildList.add(jwtAccessTokenConverter());
        chanChildList.add(cleanAdditionalInformationTokenEnhancer());
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(chanChildList);
        //组成tokenEnhance链处理
        endpoints.tokenEnhancer(tokenEnhancerChain);
        //自定义authorizationCode服务,用于生成/获取code
//        endpoints.authorizationCodeServices(null)
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    /**
     * 自定义token处理
     *
     * @return
     */
    @Bean
    public TokenEnhancer customTokenEnhancer() {
        return (accessToken, authentication) -> {
            Authentication userAuthentication = authentication.getUserAuthentication();
            if (userAuthentication != null && userAuthentication.getDetails() instanceof OAuthUserDetails) {
                OAuthUserDetails userDetails = (OAuthUserDetails) userAuthentication.getDetails();
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>(1));
                accessToken.getAdditionalInformation().put("user_id", userDetails.getDetail());
            }
            //可创建新的OAuth2AccessToken对象,此处可注释该行代码
//                accessToken = new DefaultOAuth2AccessToken(accessToken);
            return accessToken;
        };
    }

    /**
     * 清空扩展数据
     *
     * @return
     */
    @Bean
    public TokenEnhancer cleanAdditionalInformationTokenEnhancer() {
        return (accessToken, authentication) -> {
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(Collections.emptyMap());
            return accessToken;
        };
    }
}
