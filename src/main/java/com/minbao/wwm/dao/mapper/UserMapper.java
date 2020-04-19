package com.minbao.wwm.dao.mapper;

import java.util.Map;

public interface UserMapper {

    Integer insertUser(Map<String,Object> map);

    Integer updateUser(Map<String,Object> map);

    Map<String,Object> searchUser(Map<String,Object> map);

    Map<String,Object>  getUserDetail(Integer userId);
}
