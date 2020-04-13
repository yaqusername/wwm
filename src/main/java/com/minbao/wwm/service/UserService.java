package com.minbao.wwm.service;

import java.util.Map;

public interface UserService {

    String userLogin(String code);

    Integer insertUser(Map<String,Object> map);

    Map<String,Object> searchUser(Map<String,Object> map);
}
