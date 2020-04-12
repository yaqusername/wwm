package com.minbao.wwm.service;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    List<Map<String,Object>> getCategoryList();

    Map<String,Object> currentCategory(String categoryId);
}
