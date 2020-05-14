package com.minbao.wwm.controller;

import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    ReturnUtil returnUtil;

    @Resource
    OrderService orderService;

    //获取订单数量
    @RequestMapping("/orderCount")
    public Map<String,Object> orderCount(String userId){
        int errno = -1;
        String errmsg = "";
        if (StringUtils.isBlank(userId)){
            logger.error("获取订单数量用户ID不能为空");
            return returnUtil.returnResult(errno,"用户ID不能为空！",new HashMap<>());
        }
        Map<String,Object> ret;
        try {
            logger.info("获取订单数量请求参数。 userId ：" + userId);
            ret = orderService.orderCount(userId);
            if (ret != null){
                errno = 0;
                errmsg = "获取订单数量成功！";
                return returnUtil.returnResult(errno,errmsg,ret);
            }
        }catch (Exception e){
            logger.error("获取订单数量异常！msg：" + e.getMessage(),e);
            errmsg = "获取订单数量异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    //获取用户所有订单
    @RequestMapping("/getAllOrder")
    public Map<String,Object> getAllOrder(String showType ,String userId ,Integer size,Integer page){
        int errno = -1;
        String errmsg = "";
        if (StringUtils.isBlank(userId)){
            logger.error("获取所有订单用户ID不能为空");
            return returnUtil.returnResult(errno,"获取所有订单用户ID不能为空！",new HashMap<>());
        }
        if (StringUtils.isBlank(showType)){
            logger.error("获取所有订单showType不能为空");
            return returnUtil.returnResult(errno,"获取所有订单showType不能为空！",new HashMap<>());
        }
        if (size == null){
            logger.error("获取所有订单size不能为空");
            return returnUtil.returnResult(errno,"获取所有订单size不能为空！",new HashMap<>());
        }
        if (page == null){
            logger.error("获取所有订单page不能为空");
            return returnUtil.returnResult(errno,"获取所有订单page不能为空！",new HashMap<>());
        }
        page = ( page - 1 ) * size;
        List<Map<String, Object>> ret;
        try {
            logger.info("获取用户所有订单参数。 userId ：" + userId + " , showType : " + showType + ", size : " + size + ", page : " + page);
            ret = orderService.getAllOrder(userId,showType,size,page);
            if (ret != null){
                Map<String,Object> result = new HashMap<>();
                Map<String,Object> orderCount = orderService.orderTotal(userId,showType);
                int count = 0;
                if (orderCount != null && orderCount.size() > 0){
                    count = Integer.valueOf(String.valueOf(orderCount.get("count")));  //总数量
                }
                Integer totalPages = (count + size - 1) / size;  //总页数
                result.put("count",count);
                result.put("totalPages",totalPages);
                result.put("pageSize",size);
                result.put("currentPage",page + 1);
                result.put("data",ret);
                errno = 0;
                errmsg = "获取用户所有订单成功！";
                return returnUtil.returnResult(errno,errmsg,result);
            }
        }catch (Exception e){
            logger.error("获取用户所有订单异常！msg：" + e.getMessage(),e);
            errmsg = "获取用户所有订单异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    //提交订单
    @Transactional
    @RequestMapping("/submit")
    public Map<String,Object> submit(@RequestBody Map<String,Object> reqMap){
        Object addressId = reqMap.get("addressId");
        Object postscript = reqMap.get("postscript");
        Object freightPrice = reqMap.get("freightPrice");
        Object actualPrice = reqMap.get("actualPrice");
        Object offlinePay = reqMap.get("offlinePay");
        Object userId = reqMap.get("userId");
        int errno = -1;
        String errmsg = "提交订单失败！";
        if (StringUtils.isBlank(String.valueOf(userId))){
            logger.error("获取订单数量用户ID不能为空");
            return returnUtil.returnResult(errno,"用户ID不能为空！",new HashMap<>());
        }
        Map<String,Object> ret;
        try {
            logger.info("提交订单请求参数。 userId ：" + userId);
            ret = orderService.submit(userId,addressId,postscript,freightPrice,actualPrice,offlinePay);
            if (ret != null){
                errno = 0;
                errmsg = "提交订单成功！";
                return returnUtil.returnResult(errno,errmsg,ret);
            }else{
                errmsg = "订单已提交！";
            }
        }catch (Exception e){
            logger.error("提交订单异常！msg：" + e.getMessage(),e);
            errmsg = "提交订单异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    //删除订单
    @RequestMapping("/delete")
    public Map<String,Object> delete(@RequestBody  Map<String,Object> reqMap){
        Object orderId = reqMap.get("orderId");
        int errno = -1;
        String errmsg = "删除订单失败！";
        if (orderId == null){
            logger.error("orderId不能为空");
            return returnUtil.returnResult(errno,"订单ID不能为空！",new HashMap<>());
        }
        Integer ret;
        try {
            logger.info("删除订单请求参数。 oederId ：" + orderId);
            ret = orderService.delete(orderId);
            if (ret > 0){
                errno = 0;
                errmsg = "删除订单成功！";
                return returnUtil.returnResult(errno,errmsg,new HashMap());
            }
        }catch (Exception e){
            logger.error("删除订单异常！msg：" + e.getMessage(),e);
            errmsg = "删除订单异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    //取消订单
    @RequestMapping("/cancel")
    public Map<String,Object> cancel(@RequestBody  Map<String,Object> reqMap){
        Object orderId = reqMap.get("orderId");
        int errno = -1;
        String errmsg = "取消订单失败！";
        if (orderId == null){
            logger.error("订单ID不能为空");
            return returnUtil.returnResult(errno,"订单ID不能为空！",new HashMap<>());
        }
        Integer ret;
        try {
            logger.info("取消订单请求参数。 oederId ：" + orderId);
            ret = orderService.cancel(orderId);
            if (ret > 0){
                errno = 0;
                errmsg = "取消订单成功！";
                return returnUtil.returnResult(errno,errmsg,new HashMap());
            }
        }catch (Exception e){
            logger.error("取消订单异常！msg：" + e.getMessage(),e);
            errmsg = "取消订单异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }

    //订单详情
    @RequestMapping("/orderDetail")
    public Map<String,Object> orderDetail(Integer orderId,Integer userId){
        int errno = -1;
        String errmsg = "获取订单详情失败！";
        if (orderId == null){
            logger.error("订单ID不能为空");
            return returnUtil.returnResult(errno,"订单ID不能为空！",new HashMap<>());
        }
        Map<String,Object> ret;
        try {
            logger.info("获取订单详情请求参数。 oederId ：" + orderId);
            ret = orderService.orderDetail(orderId,userId);
            if (ret != null && ret.size() > 0){
                errno = 0;
                errmsg = "获取订单详情成功！";
                return returnUtil.returnResult(errno,errmsg,ret);
            }
        }catch (Exception e){
            logger.error("获取订单详情异常！msg：" + e.getMessage(),e);
            errmsg = "获取订单详情异常";
        }
        return returnUtil.returnResult(errno,errmsg,new HashMap());
    }
}