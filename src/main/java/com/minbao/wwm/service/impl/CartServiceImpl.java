package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.CartMapper;
import com.minbao.wwm.dao.mapper.OrderMapper;
import com.minbao.wwm.service.AddressService;
import com.minbao.wwm.service.CartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    CartMapper cartMapper;

    @Resource
    AddressService addressService;

    @Resource
    OrderMapper orderMapper;

    @Resource
    OrderServiceImpl orderService;

    @Override
    public Map<String, Object> goodsCount(Integer userId) {
        return cartMapper.goodsCount(userId);
    }

    @Override
    public Integer addCart(Object addType, Object productId, Object goodsId, Object number,Object userId) {
        Map<String,Object> reqMap = getAddCartInfo(productId,goodsId,number,userId);
        if (reqMap == null){
            return -1;
        }
        return cartMapper.addCart(reqMap);
    }

    @Override
    public Map<String, Object> checkedGoodsCount(Object userId) {
        return null;
    }

    @Override
    public Map<String, Object> getCartDetail(Object userId) {

        Map<String,Object> result = new HashMap<>();
        BigDecimal goodsAmount = new BigDecimal(0.00);
        BigDecimal checkedGoodsAmount = new BigDecimal(0.00);

        Map<String,Object> cartTotal = new HashMap<>();
        Map<String,Object> goodsCount = cartMapper.goodsCount(userId);
        Map<String,Object> checkedGoodsCount = cartMapper.checkedGoodsCount(userId);
        List<Map<String,Object>> getCartDetail = cartMapper.getCartDetail(userId);
        if (getCartDetail != null && getCartDetail.size() > 0){
            for (Map m:getCartDetail) {
                int num = (int) m.get("number");
                BigDecimal temp1 = BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("retail_price")))*num);
                goodsAmount = goodsAmount.add(temp1);
                if (StringUtils.equals("1",String.valueOf(m.get("checked")))){
                    BigDecimal temp = BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("retail_price")))*num);
                    checkedGoodsAmount = checkedGoodsAmount.add(temp);
                }
            }
        }
        cartTotal.put("user_id",userId);
        cartTotal.put("numberChange",0);
        cartTotal.put("goodsAmount",goodsAmount);
        cartTotal.put("checkedGoodsAmount",checkedGoodsAmount);
        cartTotal.put("goodsCount",goodsCount.get("count"));
        cartTotal.putAll(checkedGoodsCount);

        result.put("cartList",getCartDetail);
        result.put("cartTotal",cartTotal);
        return result;
    }

    @Override
    public Integer updateCart(Object userId, Object goodsId, Object number, Object productId, Object addType) {
        return cartMapper.updateCart(userId,goodsId,number,productId,addType);
    }

    @Override
    public Map<String, Object> chechAddCart(Object productId, Object goodsId, Object number, Object userId) {
        return cartMapper.chechAddCart(productId,goodsId,number,userId);
    }

    @Override
    public Integer deleteCartGoods(Object userId, Object productIds) {
        return cartMapper.deleteCartGoods(userId,productIds);
    }

    @Override
    public Integer isChecked(Object userId, List productIdList, Object isChecked) {
        return cartMapper.isChecked(userId,productIdList,isChecked);
    }

    @Override
    public Map<String, Object> checkout(Object addressId, Object addType, Object orderFrom, Object type,Object userId) {

        Map<String,Object> result = new HashMap<>();
        result.put("checkedAddress",0);
        result.put("freightPrice",0);
        //获取收获地址
        Map<String,Object> address;
        if (addressId != null && !StringUtils.equals("0",String.valueOf(addressId))){
            address = addressService.getAddressDetail(Integer.valueOf(String.valueOf(addressId)));
            if (address != null && address.size() > 0){
                StringBuilder full_region = getFullAddress(address);
                address.put("full_region",full_region);
                result.put("checkedAddress",address);
            }
        }else {
            address = addressService.getDefaultAddress(userId);
            if (address != null){
                StringBuilder full_region = getFullAddress(address);
                address.put("full_region",full_region);
                result.put("checkedAddress",address);
            }
        }
        Integer goodsCount = 0;
        if (StringUtils.equals("2",String.valueOf(addType))){
            //订单详情
            Map<String,Object> retOrder = orderMapper.orderDetail(orderFrom,userId);
            if (retOrder != null){
                result.put("freightPrice",retOrder.get("freight_price"));
                result.put("goodsTotalPrice",retOrder.get("goods_price"));
                result.put("orderTotalPrice",retOrder.get("order_price"));
                result.put("actualPrice",retOrder.get("actual_price"));
            }
            //订单商品列表
            List<Map<String,Object>> orderGoodsList = orderMapper.getOrderGoodsList(userId,orderFrom);
            if (orderGoodsList.size() > 0){
                for (Map<String,Object> map:orderGoodsList) {
                    Integer num = Integer.valueOf(String.valueOf(map.get("number")));
                    Object productId = map.get("product_id");
                    Object goodsId = map.get("goods_id");
                    goodsCount = goodsCount + num;
                    Map<String,Object> addCartInfo = getAddCartInfo(productId, goodsId, num, userId);
                    if (addCartInfo != null){
                        map.putAll(addCartInfo);
                    }
                }
                result.put("checkedGoodsList",orderGoodsList);
            }

        }else {
            //获取购物车商品列表
            List<Map<String,Object>> cartProductList = cartMapper.getCartProductList(userId);
            StringBuilder printInfo = new StringBuilder();
            BigDecimal goodsTotalPrice = new BigDecimal(0.00);
            int flag = 0;
            if (cartProductList != null && cartProductList.size() > 0){
                for (Map<String,Object> m:cartProductList) {
                    flag++;
                    Double weight_count = Double.valueOf(String.valueOf(m.get("goods_weight"))) * Integer.valueOf(String.valueOf(m.get("number")));
                    m.put("weight_count",weight_count);
                    printInfo.append(flag).append("、").append(m.get("goods_name")).append("【").append(m.get("number")).append("】 ");
                    Integer num = Integer.valueOf(String.valueOf(m.get("number")));
                    goodsCount = goodsCount + num;
                    BigDecimal tempPrice = BigDecimal.valueOf(Double.valueOf(String.valueOf(m.get("retail_price")))*num);
                    goodsTotalPrice = goodsTotalPrice.add(tempPrice);
                }
                result.put("checkedGoodsList",cartProductList);
            }
            result.put("goodsTotalPrice",goodsTotalPrice);
            result.put("orderTotalPrice",goodsTotalPrice);
            result.put("actualPrice",goodsTotalPrice);
            result.put("print_info",printInfo.toString());
        }
        result.put("numberChange",0);
        result.put("goodsCount",goodsCount);
        result.put("outStock",0);
        return result;
    }

    @Override
    public List<Map<String, Object>> getCartProductList(Object orderId) {
        return cartMapper.getCartProductList(orderId);
    }

    @Override
    public Integer updateCartGoodsStatus(Object userId) {
        return cartMapper.updateCartGoodsStatus(userId);
    }

    /**
     * 拼接省市区/县
     * @param address 地址
     * @return 省市县
     */
    private StringBuilder getFullAddress(Map<String,Object> address){
        StringBuilder full_region = new StringBuilder();
        if (StringUtils.isNotBlank(String.valueOf(address.get("province_name")))){
            full_region.append(address.get("province_name"));
        }
        if (StringUtils.isNotBlank(String.valueOf(address.get("city_name")))){
            full_region.append(address.get("city_name"));
        }
        if (StringUtils.isNotBlank(String.valueOf(address.get("district_name")))){
            full_region.append(address.get("district_name"));
        }
        return full_region;
    }

    //获取要添加购车商品信息
    private Map<String,Object> getAddCartInfo(Object productId, Object goodsId, Object number,Object userId){
        Map<String,Object> reqMap = new HashMap<>();
        Map<String,Object> cartGetGoods = cartMapper.addCartGetGoods(goodsId);
        Map<String,Object> cartGetProduct = cartMapper.addCartGetProduct(productId);
        Map<String,Object> cartGetGoodsSpecification = cartMapper.addCartGetGoodsSpecification(goodsId);
        if (cartGetGoods == null || cartGetGoodsSpecification == null || cartGetProduct == null){
            return null;
        }
        reqMap.putAll(cartGetProduct);
        reqMap.putAll(cartGetGoodsSpecification);
        reqMap.putAll(cartGetGoods);
        reqMap.put("number",number);
        reqMap.put("userId",userId);
        reqMap.put("goodsId",goodsId);
        reqMap.put("productId",productId);
        return reqMap;
    }
}
