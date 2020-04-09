package com.minbao.wwm.controller;

import com.alibaba.fastjson.JSON;
import com.minbao.wwm.common.ErrorCMD;
import com.minbao.wwm.dto.RequestJson;
import com.minbao.wwm.dto.ResponseJson;
import com.minbao.wwm.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/product")
public class ProductController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;

    /**
     * 添加产品
     * @param requestJson
     * @return
     */
    @ResponseBody
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

    @RequestMapping("/test")
    public String test(){
        String s = productService.test();
        logger.info("####################");
        return s;
    }
}
