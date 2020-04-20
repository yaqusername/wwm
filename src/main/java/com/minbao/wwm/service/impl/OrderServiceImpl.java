package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.OrderMapper;
import com.minbao.wwm.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderMapper orderMapper;

    @Override
    public Map<String, Object> orderCount(Object userId) {

        Map<String,Object> result = new HashMap<>();
        Map<String,Object> unpaid = orderMapper.unpaid(userId);
        Map<String,Object> paid = orderMapper.paid(userId);
        Map<String,Object> shipped = orderMapper.shipped(userId);

        if (unpaid != null){
            result.putAll(unpaid);
        }else {
            result.put("toPay",0);
        }
        if (paid != null){
            result.putAll(paid);
        }else {
            result.put("toDelivery",0);
        }
        if (shipped != null){
            result.putAll(shipped);
        }else {
            result.put("toReceive",0);
        }
        return result;
    }
}
