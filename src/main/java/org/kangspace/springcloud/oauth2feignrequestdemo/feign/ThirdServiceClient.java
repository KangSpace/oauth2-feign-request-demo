package org.kangspace.springcloud.oauth2feignrequestdemo.feign;

import org.kangspace.springcloud.oauth2feignrequestdemo.feign.interceptor.ThirdServiceInterceptorConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * <pre>
 * 第三方服务接口
 * (此处未配置熔断实现)
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/9/30 17:31
 */
@FeignClient(name = "thirdServiceClient", url = "${third-service.gateway.service1.url}",configuration = {ThirdServiceInterceptorConfig.class})
public interface ThirdServiceClient {

    /**
     * Feign调用接口
     * @return
     */
    @RequestMapping("/temp/resource/getAll")
    Map<String, String[]> getAll(@RequestParam("param")Map<String,String[]> param);
}
