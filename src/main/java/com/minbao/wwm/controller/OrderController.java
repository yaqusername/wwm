package com.minbao.wwm.controller;

import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.OrderService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
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
}
