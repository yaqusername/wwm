package com.minbao.wwm.dao.mapper;

import java.util.Map;

public interface OrderMapper {

    Map<String,Object> unpaid(Object userId);

    Map<String,Object> paid(Object userId);

    Map<String,Object> shipped(Object userId);
}
