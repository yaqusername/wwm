package com.minbao.wwm.controller;

import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.BannerService;
import com.minbao.wwm.service.CategoryService;
import com.minbao.wwm.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
    private static final int ERRNO = -1;

    @Autowired
    MainService mainService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BannerService bannerService;

    @Autowired
    ReturnUtil returnUtil;

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

    /**
     * 清除搜索历史
     * @return
     */
    @RequestMapping("/search/clearHistory")
    public Map<String,Object> clearHistory(@RequestBody Map<String,Object> reqMap){
        Object userId = reqMap.get("userId");
        int errno = ERRNO;
        String errmsg = "清楚历史搜索失败！";
        if (userId == null){
            return returnUtil.returnResult(ERRNO,"用户ID不能为空！",new HashMap());
        }
        try {
            logger.info("清楚历史搜索请求数据。userId ：" + userId);
            Integer ret = mainService.clearHistory(userId);
            if (ret != null && ret > 0){
                errmsg = "清楚历史搜索成功！";
                errno = 0;
            }
        }catch (Exception e){
            logger.error("清楚历史搜索异常！msg：" + e.getMessage(),e);
            errmsg = "清楚历史搜索异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    /**
     * 搜索页面数据
     * @return
     */
    @RequestMapping("/searchIndex")
    public Map<String,Object> searchIndex(String userId){
        int errno = ERRNO;
        String errmsg = "获取搜索页面数据失败！";
        Map<String,Object> data;
        if (userId == null){
            return returnUtil.returnResult(ERRNO,"用户ID不能为空！",new HashMap());
        }
        try {
            logger.info("获取搜索页面数据请求参数。userId ：" + userId);
            data = mainService.searchIndex(userId);
            if (data != null){
                errmsg = "获取搜索页面数据成功！";
                errno = 0;
                return returnUtil.returnResult(errno,errmsg,data);
            }
        }catch (Exception e){
            logger.error("获取搜索页面数据异常！msg：" + e.getMessage(),e);
            errmsg = "获取搜索页面数据异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    /**
     * 搜索帮助
     * @return
     */
    @RequestMapping("/searchHelper")
    public Map<String,Object> searchHelper(String keyword){
        int errno = ERRNO;
        String errmsg = "获取搜索帮助数据失败！";
        List<String> data;
        try {
            logger.info("获取搜索帮助数据请求参数。keyword ：" + keyword);
            data = mainService.searchHelper(keyword);
            if (data != null){
                errmsg = "获取搜索帮助数据成功！";
                errno = 0;
                return returnUtil.returnResult(errno,errmsg,data);
            }
        }catch (Exception e){
            logger.error("获取搜索帮助数据异常！msg：" + e.getMessage(),e);
            errmsg = "获取搜索帮助数据异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }
}
