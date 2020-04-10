package com.minbao.wwm.dao.mapper;

import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

@Qualifier("mysqlSessionFactory")
public interface BannerMapper {

    List<Map<String,Object>> getBanner();
}
