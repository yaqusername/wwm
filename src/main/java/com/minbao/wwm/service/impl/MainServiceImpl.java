package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.MainMapper;
import com.minbao.wwm.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainServiceImpl implements MainService {

    private static final Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);

    @Resource
    MainMapper mainMapper;

    @Override
    public Map<String, Object> showSettings() {
        return mainMapper.showSetting();
    }

    @Override
    public List<Map<String, Object>> getNotice() {
        return mainMapper.getNotice();
    }

    @Override
    public List<Map<String, Object>> getProduct() {
        return mainMapper.getProduct();
    }

    @Override
    public Integer clearHistory(Object userId) {
        return mainMapper.clearHistory(userId);
    }

    @Override
    public Map<String, Object> searchIndex(Object userId) {
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> defaultSearch = mainMapper.defaultSearch(userId);
        List<Map<String,Object>> hotSearch = mainMapper.hotSearch(userId);
        List<Map<String,Object>> historySearch = mainMapper.historySearch(userId);
        List<String> list = new ArrayList<>();
        if (historySearch != null && historySearch.size() > 0){
            for (Map m:historySearch) {
                list.add(String.valueOf(m.get("keyword")));
            }
        }
        data.put("defaultKeyword",defaultSearch);
        data.put("historyKeywordList",list);
        data.put("hotKeywordList",hotSearch);
        return data;
    }

    @Override
    public List<String> searchHelper(Object keyword) {
        return mainMapper.searchHelper(keyword);
    }

    @Override
    public Integer addSearchHistory(Object keyword, Object userId) {
        return mainMapper.addSearchHistory(keyword,userId);
    }

    @Override
    public Integer deleteSearchHistory(Object keyword, Object userId) {
        return mainMapper.deleteSearchHistory(keyword,userId);
    }
}
