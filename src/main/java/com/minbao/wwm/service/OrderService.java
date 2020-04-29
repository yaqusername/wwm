package com.minbao.wwm.service;

import java.util.Map;

public interface OrderService {

    Map<String,Object> orderCount(Object userId);

    Map<String,Object> getAllOrder(String userId,String showType,Integer size, Integer page);

    Map<String,Object> submit(Object userId,Object addressId,Object postscript,Object freightPrice,Object actualPrice,Object offlinePay);

    Integer delete(Object id);

    Integer cancel(Object id);

    Map<String,Object> orderDetail(Object orderId,Object userId);
}
