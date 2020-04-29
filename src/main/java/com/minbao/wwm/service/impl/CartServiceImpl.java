package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.CartMapper;
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

    @Override
    public Map<String, Object> goodsCount(Integer userId) {
        return cartMapper.goodsCount(userId);
    }

    @Override
    public Integer addCart(Object addType, Object productId, Object goodsId, Object number,Object userId) {

        Map<String,Object> reqMap = new HashMap<>();
        Map<String,Object> cartGetGoods = cartMapper.addCartGetGoods(goodsId);
        Map<String,Object> cartGetProduct = cartMapper.addCartGetProduct(productId);
        Map<String,Object> cartGetGoodsSpecification = cartMapper.addCartGetGoodsSpecification(goodsId);
        if (cartGetGoods != null || cartGetGoodsSpecification != null || cartGetProduct != null){
            return -1;
        }
        reqMap.putAll(cartGetProduct);
        reqMap.putAll(cartGetGoodsSpecification);
        reqMap.putAll(cartGetGoods);
        reqMap.put("number",number);
        reqMap.put("userId",userId);
        reqMap.put("goodsId",goodsId);
        reqMap.put("productId",productId);
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
    public Integer isChecked(Object userId, Object productIds, Object isChecked) {
        return cartMapper.isChecked(userId,productIds,isChecked);
    }

    @Override
    public Map<String, Object> checkou(Object addressId, Object addType, Object orderFrom, Object type,Object userId) {

        Map<String,Object> result = new HashMap<>();
        result.put("checkedAddress",0);
        result.put("freightPrice",0);
        //获取收获地址
        Map<String,Object> address;
        if (addressId != null && !StringUtils.equals("0",String.valueOf(addressId))){
            address = addressService.getAddressDetail(Integer.valueOf(String.valueOf(addressId)));
            if (address != null && address.size() > 0){
                result.put("checkedAddress",address);
            }
        }else {
            address = addressService.getDefaultAddress(userId);
            if (address != null){
                result.put("checkedAddress",address);
            }
        }

        //获取购物车商品列表
        List<Map<String,Object>> cartProductList = cartMapper.getCartProductList(userId);
        Integer goodsCount = 0;
        BigDecimal goodsTotalPrice = new BigDecimal(0.00);
        if (cartProductList != null && cartProductList.size() > 0){
            for (Map<String,Object> m:cartProductList) {
                Double weight_count = Double.valueOf(String.valueOf(m.get("goods_weight"))) * Integer.valueOf(String.valueOf(m.get("number")));
                m.put("weight_count",weight_count);
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
        result.put("goodsCount",goodsCount);
        result.put("outStock",0);
        result.put("numberChange",0);
        return result;
    }

    @Override
    public List<Map<String, Object>> getCartProductList(Object userId) {
        return cartMapper.getCartProductList(userId);
    }

    @Override
    public Integer updateCartGoodsStatus(Object userId) {
        return cartMapper.updateCartGoodsStatus(userId);
    }
}
