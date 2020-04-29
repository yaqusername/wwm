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

    @Override
    public Map<String, Object> getAddressDetail(Integer id) {
        return addressMapper.getAddressDetail(id);
    }

    @Override
    public Integer addAddress(Map<String, Object> reqMap) {
        return addressMapper.addAddress(reqMap);
    }

    @Override
    public Integer deleteAddress(Map<String, Object> reqMap) {
        return addressMapper.deleteAddress(reqMap);
    }

    @Override
    public Integer updateAddress(Map<String, Object> reqMap) {
        return addressMapper.updateAddress(reqMap);
    }

    @Override
    public Map<String, Object> getDefaultAddress(Object userId) {
        return addressMapper.getDefaultAddress(userId);
    }
}
