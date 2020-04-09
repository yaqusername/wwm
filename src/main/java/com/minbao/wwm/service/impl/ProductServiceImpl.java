package com.minbao.wwm.service.impl;

import com.minbao.wwm.common.ErrorCMD;
import com.minbao.wwm.dao.mapper.ProductMapper;
import com.minbao.wwm.dto.ResponseJson;
import com.minbao.wwm.service.ProductService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Resource
    ProductMapper productMapper;

    @Override
    public String test() {
        return "调用成功！";
    }

    @Override
    public ResponseJson addProduct(Map<String, Object> reqMap) {

//        Object productName = reqMap.get("productName");
//        Object productImage = reqMap.get("productImage");
//        Object expirationDate = reqMap.get("expirationDate");
//        Object savingMode = reqMap.get("savingMode");
//        Object description = reqMap.get("description");
        Object price = reqMap.get("price");
        Object newPrice = reqMap.get("newPrice");
        Object startTime = reqMap.get("startTime");
        Object endTime = reqMap.get("endTime");
        String msg = "产品添加成功！";
        try {
            if (StringUtils.isNotBlank(String.valueOf(newPrice))){
                if (StringUtils.isBlank(String.valueOf(startTime)) || StringUtils.isBlank(String.valueOf(endTime))){
                    return new ResponseJson(ErrorCMD.ERROR,"开始时间或者结束时间不能为空！",new HashMap<>());
                }
            }
            if ("".equals(newPrice) || newPrice  == null){
                reqMap.put("newPrice",0.0);
            }
            if ("".equals(price) || price  == null){
                reqMap.put("price",0.0);
            }
            int ret = productMapper.addProduct(reqMap);
            if (ret > 0){
                logger.info("********addProduct:"+msg);
                return new ResponseJson(ErrorCMD.SUCCESS,msg,new HashMap<>());
            }else {
                msg = "产品添加失败!";
                logger.warn("********addProduct:"+msg);
                return new ResponseJson(ErrorCMD.ERROR,msg,new HashMap<>());
            }
        }catch (Exception e){
            logger.error("********addProduct 添加产品异常！ msg:" + e.getMessage());
            return new ResponseJson(ErrorCMD.ERROR,"产品添加异常！",new HashMap<>());
        }
    }
}
