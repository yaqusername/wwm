package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface ProductMapper {

    Integer addProduct(Map<String,Object> reqMap);

    Map<String,Object> count(Map<String,Object> reqMap);

    List<Map<String,Object>> getProductByCategoryId(Map<String,Object> reqMap);
}
