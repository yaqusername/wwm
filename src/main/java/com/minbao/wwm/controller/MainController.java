package com.minbao.wwm.controller;

import com.minbao.wwm.dto.ResponseJson;
import com.minbao.wwm.service.BannerService;
import com.minbao.wwm.service.CategoryService;
import com.minbao.wwm.service.MainService;
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
@RequestMapping("/index")
public class MainController {

    private static final Logger  logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    MainService mainService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BannerService bannerService;

    /**
     * 获取前端展示设置
     * @return
     */
    @ResponseBody
    @RequestMapping("/showSettings")
    public Map<String,Object> showSetting(){

        Map<String,Object> map = new HashMap();
        Map<String,Object> result = mainService.showSettings();
        map.put("errno",0);
        map.put("errmsg","");
        map.put("data",result);
        return map;
    }

    /**
     * 首页数据接口
     * @return
     */
    @RequestMapping("/indexInfo")
    public Map<String,Object> appInfo(){

        Map<String,Object> result = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        try {
            List<Map<String,Object>> channel = categoryService.getCategory();
            List<Map<String,Object>> categoryList = categoryService.getCategoryList();
            List<Map<String,Object>> notice = mainService.getNotice();
            List<Map<String,Object>> banner = bannerService.getBanner();
            data.put("channel",channel);
            data.put("categoryList",categoryList);
            data.put("banner",banner);
            data.put("notice",notice);
            result.put("errno",0);
            result.put("errmsg","");
            result.put("data",data);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }
}
