package com.google.guava.Controller;

import com.google.guava.Utils.GuavaUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GuavaController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(GuavaUtil.class);
    Map<String, String> map = new HashMap<>();
    {
        map.put("name", "xsh");
        map.put("age", "23");
        map.put("gender", "male");
    }

    @RequestMapping("/guava/{key}")
    @ResponseBody
    public String guavaTest(@PathVariable("key") String key) {
        String result;
        if ((GuavaUtil.getKey(key, null)) == null) {
            GuavaUtil.setKey(key,map.get(key));
            result= map.get(key);
        } else {

            result= (String) GuavaUtil.getKey(key, null);
            logger.info("从缓存中获取[{}]的值为:[{}]",key,result);
        }
        return result;
    }
}
