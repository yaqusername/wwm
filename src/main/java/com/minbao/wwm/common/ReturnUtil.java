package com.minbao.wwm.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReturnUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReturnUtil.class);

    public Map<String,Object> returnResult(int errno, String errmsg, List list){
        Map<String,Object> result = new HashMap<>();
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",list);
        logger.info("返回结果。result ：" + JSON.toJSONString(result));
        return result;
    }

    public Map<String,Object> returnResult(int errno, String errmsg, Map map){
        Map<String,Object> result = new HashMap<>();
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",map);
        logger.info("返回结果 result ：" + JSON.toJSONString(result));
        return result;
    }
}
