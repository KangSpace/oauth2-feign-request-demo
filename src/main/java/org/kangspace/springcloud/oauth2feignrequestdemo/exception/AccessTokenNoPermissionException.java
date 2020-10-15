package org.kangspace.springcloud.oauth2feignrequestdemo.exception;

import java.text.MessageFormat;

/**
 * <pre>
 * AccessToken无权限异常
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/14 16:33
 */
public class AccessTokenNoPermissionException extends RuntimeException {
    private String appId;
    private String appSecret;
    private String username;
    private String password;
    private String message;

    private AccessTokenNoPermissionException() {}

    public AccessTokenNoPermissionException(String appId, String appSecret, String username, String password, String msg) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.username = username;
        this.password = password;
        this.setMessage(getAppInfo()+" error:"+msg);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String getAppInfo(){
        return MessageFormat.format("[appId: {0}, appSecret:{1}, username:{2}, password:{3} ]", appId, appSecret, username, password);
    }
}
