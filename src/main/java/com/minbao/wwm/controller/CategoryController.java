package com.minbao.wwm.controller;

import com.minbao.wwm.service.CategoryService;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
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
        List<Map<String,Object>> ret = null;
        try {
            ret = categoryService.getCategory();
        }catch (Exception e){
            logger.error(e.getMessage());
        }

        Map<String,Object> result = new HashMap<>();
        result.put("channel",ret);
        return result;
    }
}
