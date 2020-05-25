package com.minbao.wwm.service;


import java.util.List;
import java.util.Map;

public interface MainService {
    Map<String,Object> showSettings();

    List<Map<String,Object>> getNotice();

    List<Map<String,Object>> getProduct();

    Integer clearHistory(Object userId);

    Map<String,Object> searchIndex(Object userId);

    List<String> searchHelper(Object keyword);

    Integer addSearchHistory(Object keyword,Object userId);

    Integer deleteSearchHistory(Object keyword,Object userId);
}
