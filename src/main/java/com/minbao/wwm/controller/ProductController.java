package com.minbao.wwm.controller;

import com.alibaba.fastjson.JSON;
import com.minbao.wwm.common.ErrorCMD;
import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.dto.RequestJson;
import com.minbao.wwm.dto.ResponseJson;
import com.minbao.wwm.service.MainService;
import com.minbao.wwm.service.ProductService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/product")
public class ProductController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;

    @Autowired
    MainService mainService;

    @Autowired
    ReturnUtil returnUtil;

    /**
     * 添加产品
     * @param requestJson
     * @return
     */
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public ResponseJson addProduct(@RequestBody RequestJson requestJson){
        try {
            Map<String, Object>  reqMap = requestJson.getCon();
            if (reqMap == null || reqMap.size() == 0){
                logger.error("********addProduct 请求数据不能为空！");
                return new  ResponseJson(ErrorCMD.ERROR,"请求数据不能为空！",null);
            }
            logger.info("********addProduct requestData:" + JSON.toJSONString(reqMap));
            return productService.addProduct(reqMap);
        }catch (Exception e){
            logger.error("********addProduct 添加产品异常！msg:" + e.getMessage());
            return new ResponseJson(ErrorCMD.ERROR,"产品添加失败！",new HashMap<>());
        }
    }

    @RequestMapping("/count")
    public Map<String,Object> count(){
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> count = new HashMap<>();
        Map<String,Object> reqMap = new HashMap<>();
        reqMap.put("categoryId",0);
        int pCount = 0;
        int errno = 0;
        String errmsg = "获取产品数量成功";
        try {
            Map<String,Object> ret = productService.count(reqMap);
            pCount = Integer.valueOf(String.valueOf(ret.get("count")));
            count.put("goodsCount",pCount);
        }catch (Exception e){
            errno = -1;
            errmsg = "获取产品数量异常！";
            count.put("goodsCount",null);
            logger.error("获取产品数量异常！ msg：" + e.getMessage());
        }
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",count);
        logger.info("获取产品数量成功！msg：" + JSON.toJSONString(result));
        return result;
    }

    @RequestMapping("/getProductByCategoryId")
    public Map<String,Object> getProductByCategoryId(@RequestBody Map<String,Object> reqData){
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> reqMap = new HashMap<>();
        List<Map<String,Object>> ret = null;
        int pageTotal = 0;
        int errno = 0;
        int count = 0;
        String errmsg = "通过分类ID获取产品列表成功";
        int currrentPage = 0;
        int size = 0;
        try {
            currrentPage = Integer.valueOf(String.valueOf(reqData.get("page")));
            size = Integer.valueOf(String.valueOf(reqData.get("size")));
            int categoryId = Integer.valueOf(String.valueOf(reqData.get("id")));
            int page = currrentPage - 1;
            reqMap.put("size",size);
            reqMap.put("currentPage",page);
            reqMap.put("categoryId",categoryId);
            Map<String,Object> countMap = productService.count(reqMap);
            count = Integer.valueOf(String.valueOf(countMap.get("count")));
            if (count%size == 0){
                pageTotal = count/size;
            }else {
                pageTotal = count/size + 1;
            }


            ret = productService.getProductByCategoryId(reqMap);
        }catch (Exception e){
            errno = -1;
            errmsg = "通过分类ID获取产品列表异常！";
            logger.error("通过分类ID获取产品列表异常！ msg：" + e.getMessage());
        }
        data.put("count",count);
        data.put("totalPages",pageTotal);
        data.put("pageSize",size);
        data.put("currentPage",currrentPage);
        data.put("data",ret);
        logger.info("通过分类ID获取产品列表成功！msg：" + JSON.toJSONString(returnUtil.returnResult(errno,errmsg,data)));
        return returnUtil.returnResult(errno,errmsg,data);
    }

    @RequestMapping(value = "/detail")
    public Map<String,Object> detail(String id){
        if (StringUtils.isBlank(id)){
            return returnUtil.returnResult(-1,"产品ID不能为空！",new HashMap());
        }
        logger.info("获取产品详情请求参数：productId："+ id);
        return productService.detail(id);
    }

    @RequestMapping("/searchList")
    public Map<String,Object> searchList(String keyword,String sort,String order,String sales,String userId){
        int errno = -1;
        String errmsg = "搜索商品失败！";
        if (StringUtils.isBlank(keyword)){
            return returnUtil.returnResult(errno,"请输入搜索关键字！",new HashMap());
        }
        if (StringUtils.isBlank(sort)){
            return returnUtil.returnResult(errno,"sort不能为空！",new HashMap());
        }
        if (StringUtils.isBlank(order)){
            return returnUtil.returnResult(errno,"order不能为空！",new HashMap());
        }
        if (StringUtils.isBlank(sales)){
            return returnUtil.returnResult(errno,"sales不能为空！",new HashMap());
        }
        List<Map<String,Object>> ret = new ArrayList<>();
        try {
            ret = productService.searchList(keyword,sort,order,sales);
            if (ret != null){
                errmsg = "搜索商品成功！";
                //删除重复的搜索记录
                mainService.deleteSearchHistory(keyword,userId);
                //搜索成功后添加搜索记录
                mainService.addSearchHistory(keyword,userId);
                return returnUtil.returnResult(0,errmsg,ret);
            }
        }catch (Exception e){
            errno = -1;
            errmsg = "搜索商品异常！";
            logger.error("搜索商品异常！ msg：" + e.getMessage());
        }
        return returnUtil.returnResult(errno,errmsg,ret);
    }
}
