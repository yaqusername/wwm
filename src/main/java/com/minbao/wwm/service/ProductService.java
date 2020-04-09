package com.minbao.wwm.service;

import com.minbao.wwm.dto.ResponseJson;

import java.util.Map;

public interface ProductService {

    String test();

    ResponseJson addProduct(Map<String,Object> reqMap);
}
