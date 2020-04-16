package com.minbao.wwm.service.impl;

import com.minbao.wwm.dao.mapper.AddressMapper;
import com.minbao.wwm.service.AddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    AddressMapper addressMapper;

    @Override
    public List<Map<String, Object>> getRegion(String parentId) {
        return addressMapper.getRegion(parentId);
    }

    @Override
    public List<Map<String, Object>> getAddress(Integer userId) {
        return addressMapper.getAddress(userId);
    }
}
