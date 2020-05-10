package com.minbao.wwm.controller;

import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.AddressService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/address")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

    @Resource
    AddressService addressService;

    @Resource
    ReturnUtil returnUtil;

    /**
     * 通过区域parentId获取区域
     * @return Map
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
     * @return Map
     */
    @RequestMapping("/getAddress")
    public Map<String,Object> getAddress(String userId){
        List<Map<String,Object>> ret = new ArrayList<>();
        int errno = -1;
        String errmsg = "未获取到地址！";
        if (userId == null){
            return returnUtil.returnResult(errno,"userId不能为空！",new ArrayList());
        }
        if (StringUtils.equals("undefined",userId)){
            userId = "0";
        }
        try {
            logger.info("获取地址参数userId：" + userId);
            ret = addressService.getAddress(Integer.valueOf(userId));
            logger.info("获取地址结果 result：" + ret);
            if (ret != null){
                errmsg = "获取地址成功！";
                errno = 0;
            }
            if (ret.size() > 0){
                for (Map<String,Object> m:ret) {
                    m.put("full_region",((m.get("province_name"))) + " " + (m.get("city_name")) + " " + m.get("district_name"));
                }
            }
        }catch (Exception e){
            logger.error("获取地址异常！msg：" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,ret);
    }

    @RequestMapping("/getAddressDetail")
    public Map<String,Object> getAddressDetail(Integer id){
        int errno = -1;
        String errmsg = "";
        Map<String,Object> ret = new HashMap<>();
        try {
            ret = addressService.getAddressDetail(id);
            StringBuilder full_region = new StringBuilder();
            if (ret != null){
                if (ret.get("province_name") != null){
                    full_region.append(ret.get("province_name"));
                }
                if (ret.get("city_name") != null){
                    full_region.append(ret.get("city_name"));
                }
                if (ret.get("district_name") != null){
                    full_region.append(ret.get("district_name"));
                }
                ret.put("full_region",full_region);
            }
            errno = 0;
            errmsg = "获取地址详情成功！";
        }catch (Exception e){
            logger.error("获取地址详情异常！msg：" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,ret);
    }

    //添加或修改收货地址
    @RequestMapping("/addAddress")
    public Map<String,Object> addAddress(@RequestBody Map<String,Object> reqMap){
        int errno = -1;
        String errmsg = "添加收获地址失败！";
        Integer ret;
        try {
            Object id = reqMap.get("id");
            if (StringUtils.equals("true", String.valueOf(reqMap.get("is_default"))) || StringUtils.equals("1", String.valueOf(reqMap.get("is_default")))){
                addressService.clearDefaultAddress(reqMap.get("userId"));
            }
            if (id != null){
                ret = addressService.updateAddress(reqMap);
                if (ret != null && ret > 0){
                    errno = 0;
                    errmsg = "更新收获地址成功！";
                }
            }else {
                ret = addressService.addAddress(reqMap);
                if (ret != null && ret > 0){
                    errno = 0;
                    errmsg = "添加收获地址成功！";
                }
            }
            logger.info("添加或修改收货地址返回参数："+ret);
        }catch (Exception e){
            logger.error("添加或更新收获地址异常！msg：" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    //删除收货地址
    @RequestMapping("/deleteAddress")
    public Map<String,Object> deleteAddress(@RequestBody Map<String,Object> reqMap){
        int errno = -1;
        String errmsg = "";
        Integer ret;
        try {
            ret = addressService.deleteAddress(reqMap);
            if (ret != null && ret > 0){
                errno = 0;
                errmsg = "获取地址详情成功！";
            }
        }catch (Exception e){
            logger.error("获取地址详情异常！msg：" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }
}
