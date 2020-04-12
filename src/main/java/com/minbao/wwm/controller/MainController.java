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

import java.util.ArrayList;
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
            List<Map<String,Object>> channel = new ArrayList<>();
            List<Map<String,Object>> categoryList = categoryService.getCategoryList();
            List<Map<String,Object>> notice = mainService.getNotice();
            List<Map<String,Object>> banner = bannerService.getBanner();
            List<Map<String,Object>> productList = mainService.getProduct();
            for (Map map:categoryList) {
                Map<String,Object> tempMap = new HashMap<>();
                tempMap.put("icon_url",map.get("icon_url"));
                tempMap.put("is_category",map.get("is_category"));
                tempMap.put("keywords",map.get("keywords"));
                tempMap.put("level",map.get("level"));
                tempMap.put("banner",map.get("banner"));
                tempMap.put("front_name",map.get("front_name"));
                tempMap.put("is_show",map.get("is_show"));
                tempMap.put("front_desc",map.get("front_desc"));
                tempMap.put("img_url",map.get("img_url"));
                tempMap.put("name",map.get("name"));
                tempMap.put("is_channel",map.get("is_channel"));
                tempMap.put("show_index",map.get("show_index"));
                tempMap.put("height",map.get("height"));
                tempMap.put("id",map.get("id"));
                tempMap.put("sort_order",map.get("sort_order"));
                channel.add(tempMap);
            }
            for (Map m:categoryList) {
                List<Map<String,Object>> goodsList = new ArrayList<>();
                for (Map m1:productList) {
                    if (m.get("id").toString().equals(m1.get("category_id").toString())){
                        goodsList.add(m1);
                    }
                }
                m.put("goodsList",goodsList);
            }
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
