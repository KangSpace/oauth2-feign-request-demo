package org.kangspace.springcloud.oauth2feignrequestdemo.controller;

import org.kangspace.springcloud.oauth2feignrequestdemo.feign.ThirdServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/15 15:11
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private ThirdServiceClient thirdServiceClient;
    @RequestMapping("getAll")
    public Map<String, String[]> getAll(HttpServletRequest request) {
        return thirdServiceClient.getAll(request.getParameterMap());
    }
}
