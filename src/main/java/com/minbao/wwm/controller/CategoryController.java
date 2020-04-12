package com.minbao.wwm.controller;

import com.alibaba.fastjson.JSON;
import com.minbao.wwm.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;


    @RequestMapping("/getCategory")
    @ResponseBody
    public Map<String,Object> getCategory(){
        Map<String,Object> data = new HashMap<>();
        List<Map<String,Object>> ret = null;
        int errno = 0;
        String errmsg = "获取产品分类成功";
        try {
            ret = categoryService.getCategoryList();
        }catch (Exception e){
            errno = -1;
            errmsg = "获取产品分类异常！";
            logger.error("获取产品分类异常！ msg：" + e.getMessage());
        }
        data.put("categoryList",ret);
        logger.info("获取产品分类成功！msg：" + JSON.toJSONString(getResult(errno,errmsg,data)));
        return getResult(errno,errmsg,data);
    }

    @RequestMapping("/currentCategory")
    @ResponseBody
    public Map<String,Object> currentCategory(String id){
        Map<String,Object> ret = null;
        Map<String,Object> result = new HashMap<>();
        int errno = 0;
        String errmsg = "获取当前分类成功";
        try {
            ret = categoryService.currentCategory(id);
        }catch (Exception e){
            errno = -1;
            errmsg = "获取当前分类异常！";
            logger.error("获取当前分类异常！ msg：" + e.getMessage());
        }
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",ret);
        logger.info("获取当前分类成功！msg：" + JSON.toJSONString(ret));
        return result;
    }

    private Map<String,Object> getResult(int errno,String errmsg,Map data){
        Map<String,Object> result = new HashMap<>();
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",data);
        return result;
    }
}
