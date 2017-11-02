package com.app.service.impl;

import com.app.mapper.DevUserMapper;
import com.app.pojo.DevUser;
import com.app.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {
    @Autowired(required = false)
    private DevUserMapper devUserMapper;

    public DevUserMapper getDevUserMapper() {
        return devUserMapper;
    }

    public void setDevUserMapper(DevUserMapper devUserMapper) {
        this.devUserMapper = devUserMapper;
    }

    @Override
    public int add(DevUser devUser) {
        return devUserMapper.add(devUser);
    }

    @Override
    public int deleteDevUserById(Integer id) {
        return devUserMapper.deleteDevUserById(id);
    }

    @Override
    public int update(DevUser devUser) {
        return devUserMapper.update(devUser);
    }

    @Override
    public int getDevUserCount(Map<String, Object> map) {
        return devUserMapper.getDevUserCount(map);
    }

    @Override
    public DevUser getDevUserById(Integer id) {
        return devUserMapper.getDevUserById(id);
    }

    @Override
    public List<DevUser> getDevUserListByObj(DevUser devUser) {
        return devUserMapper.getDevUserListByObj(devUser);
    }

    @Override
    public List<DevUser> getDevUserPageByMap(Map<String, Object> map) {
        return devUserMapper.getDevUserPageByMap(map);
    }

    @Override
    public DevUser getDevUserByMap(Map<String, Object> map) {
        return devUserMapper.getDevUserByMap(map);
    }
}
