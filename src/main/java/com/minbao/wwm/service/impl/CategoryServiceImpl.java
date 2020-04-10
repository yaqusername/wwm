package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.CategoryMapper;
import com.minbao.wwm.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public List<Map<String, Object>> getCategory() {
        return categoryMapper.getCategory();
    }

    @Override
    public List<Map<String, Object>> getCategoryList() {
        return categoryMapper.getCategoryList();
    }
}
