package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface ProductMapper {

    Integer addProduct(Map<String,Object> reqMap);
}
