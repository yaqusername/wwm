package com.minbao.wwm.controller;

import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.AddressService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    AddressService addressService;

    @Autowired
    ReturnUtil returnUtil;

    /**
     * 通过区域parentId获取区域
     * @return
     */
    @RequestMapping("/getRegion")
    public Map<String,Object> getRegion(String parentId){
        List<Map<String,Object>> ret = new ArrayList<>();
        int errno = -1;
        String errmsg = "获取区域失败！";
        if (StringUtils.isBlank(parentId)){
            return returnUtil.returnResult(errno,"parentId不能为空！",new ArrayList());
        }
        try {
            logger.info("获取区域参数parentId：" + parentId);
            ret = addressService.getRegion(parentId);
            logger.info("获取区域结果 result：" + ret);
            if (ret.size() > 0){
                errmsg = "获取区域成功！";
                errno = 0;
            }
        }catch (Exception e){
            logger.error("获取区域异常！msg：" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,ret);
    }

    /**
     * 通过userId获取收货地址
     * @return
     */
    @RequestMapping("/getAddress")
    public Map<String,Object> getAddress(Integer userId){
        List<Map<String,Object>> ret = new ArrayList<>();
        int errno = -1;
        String errmsg = "获取地址失败！";
        if (userId == null){
            return returnUtil.returnResult(errno,"userId不能为空！",new ArrayList());
        }
        try {
            logger.info("获取地址参数userId：" + userId);
            ret = addressService.getAddress(userId);
            logger.info("获取地址结果 result：" + ret);
            if (ret.size() > 0){
                errmsg = "获取地址成功！";
                errno = 0;
            }
        }catch (Exception e){
            logger.error("获取地址异常！msg：" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,ret);
    }
}
