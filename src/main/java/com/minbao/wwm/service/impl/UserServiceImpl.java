package com.minbao.wwm.service.impl;

import com.minbao.wwm.controller.UserController;
import com.minbao.wwm.dao.mapper.UserMapper;
import com.minbao.wwm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    UserMapper userMapper;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String userLogin(String code) {

        String APPID = "wx7b6805ecb2838f04";
        String SECRET = "c41ef6ce54e1378034a4407bc07e3164";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+APPID+"&secret="+SECRET+"&js_code="+code+"&grant_type=authorization_code";

        return restTemplate.getForObject(url,String.class);
    }

    @Override
    public Integer insertUser(Map<String, Object> map) {
        return userMapper.insertUser(map);
    }

    @Override
    public Integer updateUser(Map<String, Object> map) {
        return userMapper.updateUser(map);
    }

    @Override
    public Map<String, Object> searchUser(Map<String, Object> map) {
        return userMapper.searchUser(map);
    }
}
