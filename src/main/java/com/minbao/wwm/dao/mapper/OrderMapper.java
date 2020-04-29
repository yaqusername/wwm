package com.minbao.wwm.dao.mapper;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    Map<String,Object> unpaid(Object userId);

    Map<String,Object> paid(Object userId);

    Map<String,Object> shipped(Object userId);

    List<Map<String,Object>> getAllOrder(String userId, String showType, Integer size, Integer page);

    Integer addOrder(Map<String,Object> reqMap);

    Integer addOrderGoods(Map<String,Object> reqMap);

    Integer delete(Object id);

    Integer cancel(Object id);

    Map<String,Object> orderDetail(Object orderId,Object userId);

    List<Map<String,Object>> getOrderGoods(Object userId,Object orderId);
}
