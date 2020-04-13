package com.minbao.wwm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {


    @ResponseBody
    @RequestMapping("/goodsCount")
    public Map goodsCount(){

        Map<String,Object> result = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> count = new HashMap<>();
        count.put("goodsCount",9);
        data.put("cartTotal",count);
        result.put("errno",0);
        result.put("errmsg","");
        result.put("data",data);
        return result;
    }

    public static void main(String[] a){
        int i = 15;
        int p = 14;
        if (i%p == 0){
            System.err.println(i/p);
        }else {
            System.err.println(i/p + 1);
        }
    }
}
