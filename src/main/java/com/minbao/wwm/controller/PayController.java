package com.minbao.wwm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
public class PayController {


    @RequestMapping("/preWeixinPay")
    Map<String,Object> preWeixinPay(){

        Map<String,Object> result = new HashMap<>();
        result.put("status",-1);
        result.put("data",null);

        return result;
    }



    @RequestMapping("/preALiPay")
    Map<String,Object> preALiPay(){

        Map<String,Object> result = new HashMap<>();
        result.put("status",-1);
        result.put("data",null);

        return result;
    }
}
