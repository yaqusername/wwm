package com.minbao.wwm.service;

import java.util.List;
import java.util.Map;

public interface CartService {

    Map<String,Object> goodsCount(Integer userId);

    Integer addCart(Object addType,Object productId,Object goodsId,Object number,Object userId);

    Map<String,Object> checkedGoodsCount(Object userId);

    Map<String,Object> getCartDetail(Object userId);

    Integer updateCart(Object userId,Object goodsId,Object number,Object productId,Object addType);

    Map<String,Object> chechAddCart(Object productId,Object goodsId,Object number,Object userId);

    Integer deleteCartGoods(Object userId,Object productIds);

    Integer isChecked(Object userId,Object productIds,Object isChecked);

    Map<String,Object> checkout(Object addressId,Object addType,Object orderFrom,Object type,Object userId);

    List<Map<String,Object>> getCartProductList(Object userId);

    Integer updateCartGoodsStatus(Object userId);
}
