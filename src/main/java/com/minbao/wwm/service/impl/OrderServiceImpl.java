package com.minbao.wwm.service.impl;

import com.alibaba.fastjson.JSON;
import com.minbao.wwm.dao.mapper.AddressMapper;
import com.minbao.wwm.dao.mapper.OrderMapper;
import com.minbao.wwm.service.CartService;
import com.minbao.wwm.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    OrderMapper orderMapper;

    @Autowired
    CartService cartService;

    @Resource
    AddressMapper addressMapper;

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

    @Override
    public List<Map<String, Object>> getAllOrder(String userId, String showType, Integer size, Integer page) {

        //订单列表
        List<Map<String,Object>> orderList = orderMapper.getAllOrder(userId,showType,size,page);
        if (orderList != null && orderList.size() > 0){
            for (Map<String,Object> map:orderList) {
                Object orderId = map.get("id");
                List<Map<String,Object>> ret = orderMapper.getOrderGoods(userId,orderId);
                if (ret.size() > 0){
                    map.put("goodsList",ret);
                }
                Map<String,Object> count = orderMapper.orderCount(userId,showType,size,page);
                if (count != null && count.size() > 0){
                    map.put("goodsCount",Integer.valueOf(String.valueOf(count.get("count"))));
                }
                map.put("handleOption",new HashMap<>());
            }
        }
        return orderList;
    }

    @Override
    public Map<String, Object> submit(Object userId, Object addressId, Object postscript, Object freightPrice, Object actualPrice, Object offlinePay) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        double d1 = Math.random();
        double d2 = Math.random();
        String s1 = (int)(d1*100)+"";
        if (s1.length() == 3 || s1.length() == 1){
            s1 = "88";
        }
        String s2 = (int)(d2*10)+"";
        if (s2.length() == 2){
            s2 = "6";
        }
        List<Map<String,Object>> addGoodsMapList;
        Map<String,Object> reqMap = new HashMap<>();
        Map<String,Object> ret = new HashMap<>();
        Integer result;
        Object orderId = 0;
        reqMap = cartService.checkout(addressId,null,null,null,userId);
        String orderNo = "ON" + dateFormat.format(new Date()) + s1 + s2;
        if (reqMap.get("checkedAddress") != null){
            reqMap.putAll((Map)reqMap.get("checkedAddress"));
        }
        reqMap.put("orderNo",orderNo);
        reqMap.put("mark",postscript);
        reqMap.put("freightPrice",freightPrice);
        reqMap.put("actualPrice",actualPrice);
        reqMap.put("offlinePay",offlinePay);
        addGoodsMapList = (List<Map<String, Object>>) reqMap.get("checkedGoodsList");
        if (addGoodsMapList == null){
            return null;
        }
        logger.info("add订单请求参数：" + JSON.toJSONString(reqMap));
        result = orderMapper.addOrder(reqMap);
        logger.info("add订单返回结果：" + result);
        if (result != null && result > 0 && addGoodsMapList.size() > 0){
            orderId = reqMap.get("id");
            for (Map<String,Object> m:addGoodsMapList) {
                m.put("orderId",reqMap.get("id"));
                m.put("userId",userId);
                orderMapper.addOrderGoods(m);
            }
        }
        //更新购物车商品提交订单后状态
        cartService.updateCartGoodsStatus(userId);
        ret = orderMapper.orderDetail(orderId,userId);
        Map<String,Object> retMap = new HashMap<>();
        if (ret != null){
            retMap.put("orderInfo",reqMap);
        }
        return retMap;
    }

    @Override
    public Integer delete(Object id) {
        return orderMapper.delete(id);
    }

    @Override
    public Integer cancel(Object id) {
        return orderMapper.cancel(id);
    }

    @Override
    public Map<String, Object> orderDetail(Object orderId, Object userId) {

        Map<String,Object> result = new HashMap<>();
        Map<String,Object> orderDetail = orderMapper.orderDetail(orderId,userId);
        if (orderDetail == null){
            return orderDetail;
        }
        Object province = orderDetail.get("province");
        Object city = orderDetail.get("city");
        Object district = orderDetail.get("district");
        Object address = orderDetail.get("address");
        if (addressMapper.getAreaName(province) != null){
            orderDetail.put("province_name",addressMapper.getAreaName(province));
        }
        if (addressMapper.getAreaName(city) != null){
            orderDetail.put("city_name",addressMapper.getAreaName(city));
        }
        if (addressMapper.getAreaName(district) != null){
            orderDetail.put("district_name",addressMapper.getAreaName(district));
        }
        if (addressMapper.getAreaName(address) != null){
            orderDetail.put("full_region",addressMapper.getAreaName(address));
        }
        Object orderStatus = orderDetail.get("order_status");
        if ("101".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","待付款");
        }
        if ("102".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","交易取消");
        }
        if ("201".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","待发货");
        }
        if ("202".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","退款中");
        }
        if ("203".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","已退款");
        }
        if ("301".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","已发货");
        }
        if ("302".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","已收货");
        }
        if ("401".equals(orderStatus.toString())){
            orderDetail.put("order_status_text","已完成");
        }

        orderDetail.put("final_pay_time","2020-08-08 08:08:00");
        result.put("orderInfo",orderDetail);

        List<Map<String,Object>> ret = orderMapper.getOrderGoods(userId,orderId);
        Integer goodsCount = 0;
        if (ret != null && ret.size() > 0){
            result.put("orderGoods",ret);
            for (Map<String,Object> m:ret) {
                goodsCount = goodsCount + Integer.valueOf(String.valueOf(m.get("number")));
            }
        }
        result.put("goodsCount",goodsCount);

        return result;
    }

}
