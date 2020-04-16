package com.minbao.wwm.service;

import java.util.List;
import java.util.Map;

public interface AddressService {

    List<Map<String,Object>> getRegion(String parentId);

    List<Map<String,Object>> getAddress(Integer userId);
}
