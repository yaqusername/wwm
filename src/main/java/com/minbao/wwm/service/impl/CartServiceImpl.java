package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.CartMapper;
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
}
