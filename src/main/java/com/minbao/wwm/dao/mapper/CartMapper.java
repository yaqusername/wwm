package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface CartMapper {

    Map<String,Object> goodsCount(Object userId);

    Integer addCart(Map<String,Object> reqMap);

    Map<String,Object> addCartGetProduct(Object productId);

    Map<String,Object> addCartGetGoods(Object goodsId);

    Map<String,Object> addCartGetGoodsSpecification(Object goodsId);

    Map<String,Object> checkedGoodsCount(Object userId);

    List<Map<String,Object>> getCartDetail(Object userId);

    Integer updateCart(Object userId,Object goodsId,Object number,Object productId,Object addType);

    Map<String,Object> chechAddCart(Object productId,Object goodsId,Object number,Object userId);

    Integer deleteCartGoods(Object userId,Object productIds);

    Integer isChecked(Object userId,List productIdList,Object isChecked);

    List<Map<String,Object>> getCartProductList(Object orderId);

    Integer updateCartGoodsStatus(Object userId);
}
