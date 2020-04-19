package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface ProductMapper {

    Integer addProduct(Map<String,Object> reqMap);

    Map<String,Object> count(Map<String,Object> reqMap);

    List<Map<String,Object>> getProductByCategoryId(Map<String,Object> reqMap);

    //商品详情
    Map<String,Object> productInfo(String productId);

    //详情列表
    List<Map<String,Object>> productList(String productId);

    //商品规格
    List<Map<String,Object>> specificationList(String productId);

    //图片资源
    List<Map<String,Object>> imageResouce(String productId);

    //产品搜索列表
    List<Map<String,Object>> searchList(String keyword,String sort,String order,String sales);
}
