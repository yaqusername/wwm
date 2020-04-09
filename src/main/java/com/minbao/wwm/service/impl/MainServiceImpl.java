package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.MainMapper;
import com.minbao.wwm.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
