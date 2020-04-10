package com.minbao.wwm.service;


import java.util.List;
import java.util.Map;

public interface MainService {
    Map<String,Object> showSettings();

    List<Map<String,Object>> getNotice();
}
