package com.minbao.wwm.controller;

import com.alibaba.fastjson.JSON;
import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final static Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    CartService cartService;

    @Autowired
    ReturnUtil returnUtil;

    /**
     * 获取购物车商品数量
     * @param userId
     * @return
     */
    @RequestMapping("/goodsCount")
    public Map goodsCount(Integer userId){
        int errno = -1;
        String errmsg = "";
        if (userId == null){
            return returnUtil.returnResult(errno,"用户ID不能为空！",new ArrayList());
        }
        Map<String,Object> result;
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> count = new HashMap<>();
        long goodsCount = 0;
        try {
            logger.info("获取购物车商品数量请求参数,userId ：" + userId);
            result = cartService.goodsCount(userId);
            if (result != null && result.get("count") != null){
                goodsCount = (long) result.get("count");
                errno = 0;
            }
        }catch (Exception e){
            logger.error("获取购物出商品数量异常！msg :" + e.getMessage(),e);
        }
        count.put("goodsCount",goodsCount);
        data.put("cartTotal",count);
        return returnUtil.returnResult(errno,errmsg,data);
    }

    /**
     * 添加购物车
     * @param reqMap
     * @return
     */
    @RequestMapping("/addCart")
    public Map addCart(@RequestBody Map<String,Object> reqMap){
        Object addType = reqMap.get("addType");
        Object productId = reqMap.get("productId");
        Object goodsId = reqMap.get("goodsId");
        Object number = reqMap.get("number");
        Object userId = reqMap.get("userId");
        int errno = -1;
        String errmsg = "";
        if (addType == null){
            return returnUtil.returnResult(errno,"addType不能为空！",new ArrayList());
        }
        if (productId == null){
            return returnUtil.returnResult(errno,"productId不能为空！",new ArrayList());
        }
        if (goodsId == null){
            return returnUtil.returnResult(errno,"goodsId不能为空！",new ArrayList());
        }
        if (number == null){
            return returnUtil.returnResult(errno,"商品数量不能为空！",new ArrayList());
        }
        if (userId == null){
            return returnUtil.returnResult(errno,"商品数量不能为空！",new ArrayList());
        }
        Integer result;
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> temp;
        try {
            logger.info("添加购物车请求参数,userId ：" + JSON.toJSONString(reqMap));
            temp = cartService.chechAddCart(productId,goodsId,number,userId);

            if (temp != null && Integer.valueOf(temp.get("count").toString()) > 0){
                result = cartService.updateCart(userId,goodsId,null,productId,null);
            }else {
                result = cartService.addCart(addType,productId,goodsId,number,userId);
            }

            if (result != null && result > 0){
                errno = 0;
                errmsg = "添加购物车成功！";
                Map<String,Object> cartDetail = cartService.getCartDetail(userId);
                if (cartDetail != null){
                    data = cartDetail;
                }
            }
        }catch (Exception e){
            logger.error("添加购物车异常！msg :" + e.getMessage(),e);
        }
        return returnUtil.returnResult(errno,errmsg,data);
    }

    /**
     * 更新购物车
     * @param reqMap
     * @return
     */
    @RequestMapping("/updateCart")
    public Map updateCart(@RequestBody Map<String,Object> reqMap){
        int errno = -1;
        String errmsg = "更新购物车失败！";
        Object userId = reqMap.get("userId");
        Object goodsId = reqMap.get("goodsId");
        Object number = reqMap.get("number");
        Object productId = reqMap.get("productId");
        Map<String,Object> data = new HashMap<>();
        try {
            if (userId == null){
                return returnUtil.returnResult(errno,"用户ID不能为空！",new HashMap());
            }
            if (goodsId == null){
                return returnUtil.returnResult(errno,"商品ID不能为空！",new HashMap());
            }
            if (number == null){
                return returnUtil.returnResult(errno,"商品数量不能为空！",new HashMap());
            }
            if (productId == null){
                return returnUtil.returnResult(errno,"产品ID不能为空！",new HashMap());
            }
            Integer ret = cartService.updateCart(userId,goodsId,number,productId,null);
            if (ret != null && ret > 0){
                errmsg = "更新购物车成功！";
                errno = 0;
                Map<String,Object> cartDetail = cartService.getCartDetail(userId);
                if (cartDetail != null){
                    data = cartDetail;
                }
            }
        }catch (Exception e){
            logger.error("更新购车异常！msg ： " + e.getMessage(),e);
            errmsg = "更新购物车异常！";
        }
        return returnUtil.returnResult(errno,errmsg,data);
    }

    /**
     * 购物车首页信息
     * @param userId
     * @return
     */
    @RequestMapping("/getCartDetail")
    public Map getCartDetail(String userId){
        int errno = -1;
        String errmsg = "获取购物车首页数据失败！";
        if (userId == null){
            return returnUtil.returnResult(errno,"userId不能为空！",new HashMap());
        }
        Map<String,Object> result = new HashMap<>();
        try {
            logger.info("获取购物车首页数据请求数据 userId ：" + userId);
            result = cartService.getCartDetail(userId);
            if (result != null){
                logger.info("获取购物车首页数据结果 result ：" + JSON.toJSONString(result));
                return returnUtil.returnResult(0,"获取购物车首页数据成功！",result);
            }
        }catch (Exception e){
            logger.error("获取购物车首页数据异常！msg" + e.getMessage(),e);
            errmsg = "获取购物车首页数据异常";
        }
        return returnUtil.returnResult(errno,errmsg,result);
    }

    /**
     * 购物车删除产品
     * @param reqMap
     * @return
     */
    @RequestMapping("/deleteCartGoods")
    public Map deleteCartGoods(@RequestBody Map<String,Object> reqMap){
        int errno = -1;
        String errmsg = "购物车删除产品失败！";
        Object userId = reqMap.get("userId");
        Object productIds = reqMap.get("productIds");
        Map<String,Object> data = new HashMap<>();
        if (userId == null){
            return returnUtil.returnResult(errno,"用户Id不能为空！",new HashMap());
        }
        if (productIds == null){
            return returnUtil.returnResult(errno,"产品Id不能为空！",new HashMap());
        }
        try {
            logger.info("购物车删除产品请求数据 userId ：" + userId);
            Integer ret = cartService.deleteCartGoods(userId,productIds);
            if (ret != null && ret > 0){
                data = cartService.getCartDetail(userId);
                if (data != null){
                    errno = 0;
                    errmsg = "购物车删除产品成功！";
                    return returnUtil.returnResult(errno,errmsg,data);
                }
            }
        }catch (Exception e){
            logger.error("购物车删除产品异常！msg:" + e.getMessage(),e);
            errmsg = "购物车删除产品异常！";
        }
        return returnUtil.returnResult(errno,errmsg,data);
    }

    /**
     * 选择或取消选择商品
     * @param reqMap
     * @return
     */
    @RequestMapping("/isChecked")
    public Map isChecked(@RequestBody Map<String,Object> reqMap){
        int errno = -1;
        String errmsg = "选择或取消选择商品产品失败！";
        Object userId = reqMap.get("userId");
        Object productIds = reqMap.get("productIds");
        Object isChecked = reqMap.get("isChecked");
        Map<String,Object> data = new HashMap<>();
        if (isChecked == null){
            return returnUtil.returnResult(errno,"是否选中不能为空！",new HashMap());
        }
        if (userId == null){
            return returnUtil.returnResult(errno,"用户Id不能为空！",new HashMap());
        }
        if (productIds == null){
            return returnUtil.returnResult(errno,"产品Id不能为空！",new HashMap());
        }
        try {
            logger.info("选择或取消选择商品请求数据 request ：" + JSON.toJSONString(reqMap));
            Integer ret = cartService.isChecked(userId,productIds,isChecked);
            if (ret != null && ret > 0){
                data = cartService.getCartDetail(userId);
                if (data != null){
                    errno = 0;
                    errmsg = "选择或取消选择商品成功！";
                    return returnUtil.returnResult(errno,errmsg,data);
                }
            }
        }catch (Exception e){
            logger.error("选择或取消选择商品异常！msg:" + e.getMessage(),e);
            errmsg = "选择或取消选择商品异常！";
        }
        return returnUtil.returnResult(errno,errmsg,data);
    }
}
