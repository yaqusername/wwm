package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface MainMapper {

    Map<String,Object> showSetting();

    List<Map<String,Object>> getNotice();

    List<Map<String,Object>> getProduct();

    Integer clearHistory(Object userId);

    Map<String,Object> defaultSearch(Object userId);

    List<Map<String,Object>> historySearch(Object userId);

    List<Map<String,Object>> hotSearch(Object userId);

    List<String> searchHelper(Object keyword);

    Integer addSearchHistory(Object keyword,Object userId);

    Integer deleteSearchHistory(Object keyword,Object userId);
}
