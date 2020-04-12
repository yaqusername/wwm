package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface MainMapper {

    Map<String,Object> showSetting();

    List<Map<String,Object>> getNotice();

    List<Map<String,Object>> getProduct();
}
