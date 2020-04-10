package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.BannerMapper;
import com.minbao.wwm.service.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BannerServiceImpl implements BannerService {

    private static final Logger logger = LoggerFactory.getLogger(BannerServiceImpl.class);

    @Resource
    BannerMapper bannerMapper;

    @Override
    public List<Map<String, Object>> getBanner() {
        return bannerMapper.getBanner();
    }
}
