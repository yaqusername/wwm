package com.minbao.wwm.common;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReturnUtil {

    public Map<String,Object> returnResult(int errno, String errmsg, List list){
        Map<String,Object> result = new HashMap<>();
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",list);
        return result;
    }

    public Map<String,Object> returnResult(int errno, String errmsg, Map map){
        Map<String,Object> result = new HashMap<>();
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",map);
        return result;
    }
}
