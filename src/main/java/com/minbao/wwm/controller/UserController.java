package com.minbao.wwm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minbao.wwm.common.ReturnUtil;
import com.minbao.wwm.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    ReturnUtil returnUtil;

    @RequestMapping("/userLogin")
    public Map<String,Object> userLogin(@RequestBody Map<String,Object> reqData){

        int errno = -1;
        String errmsg = "微信授权登录失败";
        String openid = "";
        int is_new = 1;
        String avatarUrl = "";
        String nickname = null;
        int id = 0;
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> userInfo = new HashMap<>();
        try {
            String code = String.valueOf(reqData.get("code"));
            Object userInfoObj = reqData.get("userInfo");
            JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(userInfoObj));
            userInfo = JSONObject.parseObject(String.valueOf(jsonObject.get("userInfo")));

            String ret = userService.userLogin(code);
            JSONObject retObj = JSONObject.parseObject(ret);
            if (retObj.containsKey("openid")){
                logger.info("微信授权登成功！msg："+ JSON.toJSONString(retObj));
                openid = retObj.getString("openid");
                userInfo.put("openid",openid);
                userInfo.put("sessionkey",retObj.get("session_key"));
                Map<String,Object> isMember = userService.searchUser(retObj);
                if (isMember != null){
                    is_new = 0;
                    id = Integer.valueOf(String.valueOf(isMember.get("id")));
                }else {
                    int addUser = userService.insertUser(userInfo);
                    if (addUser > 0){
                        logger.info("新用户授权成功！");
                        userInfo.put("id",userInfo.get("id"));
                    }
                }
                userInfo.put("id",id);
                errno = 0;
                errmsg = "微信授权登成功";
                avatarUrl = String.valueOf(userInfo.get("avatarUrl"));
                nickname = String.valueOf(userInfo.get("nickName"));
            }else {
                logger.error("微信授权登录失败！msg："+retObj.toString());
                errmsg = String.valueOf(retObj.get("errmsg"));
            }
        }catch (Exception e){
            logger.error("微信授权登录异常！"+e.getMessage(),e.getMessage());
        }
        data.put("is_new",is_new);
        data.put("nickname",nickname);
        data.put("avatarUrl",avatarUrl);
        data.put("openid",openid);
        data.put("userInfo",userInfo);
        data.put("id",id);
        return returnUtil.returnResult(errno,errmsg,data);
    }

    @RequestMapping("/updateUser")
    public Map<String,Object> updateUser(@RequestBody Map<String,Object> reqData) {
        int errno = -1;
        String errmsg = "用户更新失败！";
        Map<String,Object> result = new HashMap<>();
        if (reqData.get("id")== null || reqData.get("id") == ""){
            return returnUtil.returnResult(errno,"userId不能为空!",new ArrayList());
        }
        if (StringUtils.isBlank((String) reqData.get("name"))){
            return returnUtil.returnResult(errno,"姓名不能为空!",new ArrayList());
        }
        if (StringUtils.isBlank((String) reqData.get("mobile"))){
            return returnUtil.returnResult(errno,"手机号不能为空!",new ArrayList());
        }

        try {
            logger.info("更新用户请求参数："+ JSON.toJSONString(reqData));
            int ret = userService.updateUser(reqData);
            if (ret > 0){
                errno = 0;
                errmsg = "更新用户信息成功！";
                logger.info("更新用户条数："+ ret);
            }
        }catch (Exception e){
            logger.error("用户更新异常！msg："+e.getMessage());
        }
        return returnUtil.returnResult(errno,errmsg,new ArrayList());
    }
}
