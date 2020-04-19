package com.minbao.wwm.service;

import com.minbao.wwm.dto.ResponseJson;

import java.util.List;
import java.util.Map;

public interface ProductService {

    String test();

    ResponseJson addProduct(Map<String,Object> reqMap);

    Map<String,Object> count(Map<String,Object> reqMap);

    Map<String,Object> detail(String productId);

    List<Map<String,Object>> getProductByCategoryId(Map<String,Object> reqMap);

    List<Map<String,Object>> searchList(String keyword,String sort,String order,String sales);
}
