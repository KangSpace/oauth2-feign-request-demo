package org.kangspace.springcloud.oauth2feignrequestdemo.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 *
 * </pre>
 *
 * @author kango2gler@gmail.com
 * @date 2020/10/12 18:07
 */
public class WebUtil {
    public static void writeJson(HttpServletResponse response, Object o) {
        try {
            response.getWriter().write(JSONObject.toJSONString(o));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
