package com.minbao.wwm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.minbao.wwm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

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
                Map<String,Object> isMember = userService.searchUser(retObj);
                if (Integer.valueOf(String.valueOf(isMember.get("count")))>0){
                    is_new = 0;
                    id = Integer.valueOf(String.valueOf(isMember.get("id")));
                }else {
                    userService.insertUser(userInfo);
                    logger.info("新用户授权成功！");
                }
                userInfo.put("openid",retObj.get("openid"));
                userInfo.put("sessionkey",retObj.get("session_key"));

                errno = 0;
                errmsg = "微信授权登成功";
                avatarUrl = String.valueOf(userInfo.get("avatarUrl"));
                nickname = String.valueOf(userInfo.get("nickName"));
            }else {
                logger.error("微信授权登录失败！msg："+retObj.toString());
                errmsg = String.valueOf(retObj.get("errmsg"));
            }
        }catch (Exception e){
            logger.error("微信授权登录异常！"+e.getMessage());
        }

        Map<String,Object> result = new HashMap<>();
        data.put("is_new",is_new);
        data.put("nickname",nickname);
        data.put("avatarUrl",avatarUrl);
        data.put("openid",openid);
        data.put("userInfo",userInfo);
        data.put("id",id);
        result.put("errno",errno);
        result.put("errmsg",errmsg);
        result.put("data",data);
        return result;
    }
}
