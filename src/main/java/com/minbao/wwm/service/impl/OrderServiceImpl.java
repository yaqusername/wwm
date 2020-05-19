package com.minbao.wwm.service.impl;

import com.alibaba.fastjson.JSON;
import com.minbao.wwm.dao.mapper.AddressMapper;
import com.minbao.wwm.dao.mapper.OrderMapper;
import com.minbao.wwm.service.CartService;
import com.minbao.wwm.service.OrderService;
import com.minbao.wwm.util.DateUtils;
import org.apache.commons.lang.StringUtils;
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
    public Map<String, Object> orderTotal(Object userId, Object showType) {
        return orderMapper.orderCount(userId,showType);
    }

    @Override
    public List<Map<String, Object>> getAllOrder(String userId, String showType, Integer size, Integer page) {

        //订单列表
        List<Map<String,Object>> orderList = orderMapper.getAllOrder(userId,showType,size,page);
        if (orderList != null && orderList.size() > 0){
            for (Map<String,Object> map:orderList) {
                Object orderId = map.get("id");
                List<Map<String,Object>> ret = orderMapper.getOrderGoods(userId,orderId);
                int tempCount = 0;
                if (ret.size() > 0){
                    map.put("goodsList",ret);
                    for (Map<String,Object> m:ret) {
                        if (StringUtils.isNotBlank(String.valueOf(m.get("number")))){
                            tempCount = tempCount + Integer.valueOf(String.valueOf(m.get("number")));
                        }
                    }
                }
                int tempOrderStatus = Integer.valueOf(String.valueOf(map.get("order_status")));
                Map<String,Object> handleOption = getHandleOption(tempOrderStatus);

                String statusText = "待付款";

                map.put("goodsCount",tempCount);
                map.put("order_status_text",statusText);
                map.put("handleOption",handleOption);
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
            retMap.put("orderInfo",ret);
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
        String provinceStr = addressMapper.getAreaName(province);
        String cityStr = addressMapper.getAreaName(city);
        String districtStr= addressMapper.getAreaName(district);
        orderDetail.put("province_name","");
        if (StringUtils.isNotBlank(provinceStr)){
            orderDetail.put("province_name",provinceStr);
        }
        orderDetail.put("city_name","");
        if (StringUtils.isNotBlank(cityStr)){
            orderDetail.put("city_name",cityStr);
        }
        orderDetail.put("district_name","");
        if (StringUtils.isNotBlank(districtStr)){
            orderDetail.put("district_name",districtStr);
        }
        orderDetail.put("full_region","");
        if (StringUtils.isNotBlank(provinceStr) && StringUtils.isNotBlank(districtStr) && StringUtils.isNotBlank(cityStr)){
            orderDetail.put("full_region",provinceStr + cityStr + districtStr);
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
        String final_pay_time = String.valueOf(orderDetail.get("final_pay_time"));
        if (StringUtils.isNotBlank(final_pay_time)){
            long timeStemp= DateUtils.timeToStamp(final_pay_time);
            orderDetail.put("final_pay_time",timeStemp / 1000);
        }
        result.put("orderInfo",orderDetail);

        int tempOrderStatus = Integer.valueOf(String.valueOf(orderDetail.get("order_status")));
        List<Map<String,Object>> ret = orderMapper.getOrderGoods(userId,orderId);
        int goodsCount = 0;
        if (ret != null && ret.size() > 0){
            result.put("orderGoods",ret);
            for (Map<String,Object> m:ret) {
                goodsCount = goodsCount + Integer.valueOf(String.valueOf(m.get("number")));
            }
        }
        Map<String,Object> textCode = getTextCode(tempOrderStatus);
        Map<String,Object> handleOption = getHandleOption(tempOrderStatus);

        result.put("goodsCount",goodsCount);
        result.put("handleOption",handleOption);
        result.put("textCode",textCode);
        return result;
    }

    /**
     * 组装操作信息学
     * @param tempOrderStatus 订单状态
     * @return Map
     */
    private Map<String,Object> getHandleOption(Integer tempOrderStatus){
        Map<String,Object> handleOption = new HashMap<>();
        handleOption.put("cancel",false);
        handleOption.put("delete",false);
        handleOption.put("pay",false);
        handleOption.put("confirm",false);
        handleOption.put("cancel_refund",false);
        // 订单流程：下单成功－》支付订单－》发货－》收货－》评论
        // 订单相关状态字段设计，采用单个字段表示全部的订单状态
        // 1xx表示订单取消和删除等状态：  101订单创建成功等待付款、102订单已取消、103订单已取消(自动)
        // 2xx表示订单支付状态：        201订单已付款，等待发货、202订单取消，退款中、203已退款
        // 3xx表示订单物流相关状态：     301订单已发货，302用户确认收货、303系统自动收货
        // 4xx表示订单完成的状态：      401已收货已评价
        // 5xx表示订单退换货相关的状态：  501已收货，退款退货
        // 如果订单已经取消或是已完成，则可删除和再次购买
        // if (status == 101) "未付款";
        // if (status == 102) "已取消";
        // if (status == 103) "已取消(系统)";
        // if (status == 201) "已付款";
        // if (status == 301) "已发货";
        // if (status == 302) "已收货";
        // if (status == 303) "已收货(系统)";
        // 订单刚创建，可以取消订单，可以继续支付
        if (tempOrderStatus == 101 || tempOrderStatus == 801) {
            handleOption.put("cancel",true);
            handleOption.put("pay",true);
        }
        // 如果订单被取消
        if (tempOrderStatus == 102 || tempOrderStatus == 103) {
            handleOption.put("delete",true);
        }
        // 如果订单申请退款中，没有相关操作
        if (tempOrderStatus == 202) {
            handleOption.put("cancel_refund",true);
        }
        if (tempOrderStatus == 203) {
            handleOption.put("delete",true);
        }
        // 如果订单已经发货，没有收货，则可收货操作,
        // 此时不能取消订单
        if (tempOrderStatus == 301) {
            handleOption.put("confirm",true);
        }
        if (tempOrderStatus == 401) {
            handleOption.put("delete",true);
        }
        return handleOption;
    }

    /**
     * 获取订单状态信息
     * @param tempOrderStatus 状态信息
     * @return String
     */
    private String getStatusText(Integer tempOrderStatus){
        String statusText = "";
        switch (tempOrderStatus) {
            case 101:
                statusText = "待付款";
                break;
            case 102:
                statusText = "交易关闭";
                break;
            case 103:
                //到时间系统自动取消
                statusText = "交易关闭";
                break;
            case 201:
                statusText = "待发货";
                break;
            case 300:
                statusText = "待发货";
                break;
            case 301:
                statusText = "已发货";
                break;
            case 401:
                //到时间，未收货的系统自动收货、
                statusText = "交易成功";
                break;
        }
        return statusText;
    }

    /**
     *
     * @param tempOrderStatus 订单状态码
     * @return Map
     */
    private Map<String,Object> getTextCode(Integer tempOrderStatus){
        Map<String,Object> textCode = new HashMap<>();
        textCode.put("pay",false);
        textCode.put("close",false);
        textCode.put("delivery",false);
        textCode.put("receive",false);
        textCode.put("success",false);
        textCode.put("countdown",false);
        if (tempOrderStatus == 101) {
            textCode.put("countdown",true);
            textCode.put("pay",true);
        }
        if (tempOrderStatus == 102 || tempOrderStatus == 103) {
            textCode.put("close",true);
        }
        if (tempOrderStatus == 201 || tempOrderStatus == 300) {
            textCode.put("delivery",true);
        }
        if (tempOrderStatus == 301) {
            textCode.put("receive",true);
        }
        if (tempOrderStatus == 401) {
            textCode.put("success",true);
        }
        return textCode;
    }
}
