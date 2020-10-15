package org.kangspace.springcloud.oauth2feignrequestdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <pre>
 * 测试资源Controller
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/15 14:36
 */
@RestController
@RequestMapping("/temp/resource/")
public class TempResourceController {

    @RequestMapping("getAll")
    public Map<String, String[]> getAll(HttpServletRequest request) {
//        Map<String, Object> map = new HashMap<>();
        return request.getParameterMap();
    }

}
