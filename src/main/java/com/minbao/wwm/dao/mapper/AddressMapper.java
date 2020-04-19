package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface AddressMapper {

    List<Map<String,Object>> getRegion(String parentId);

    List<Map<String,Object>> getAddress(Integer userId);

    Map<String,Object> getAddressDetail(Integer userId);

    Integer addAddress(Map<String,Object> reqMap);

    Integer deleteAddress(Map<String,Object> reqMap);

    Integer updateAddress(Map<String,Object> reqMap);
}
