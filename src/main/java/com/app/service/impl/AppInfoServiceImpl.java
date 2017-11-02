package com.app.service.impl;

import com.app.mapper.AppInfoMapper;
import com.app.pojo.AppInfo;
import com.app.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("appInfoServiceImpl")
public class AppInfoServiceImpl implements AppInfoService {
    @Autowired(required = false)
    private AppInfoMapper appInfoMapper;

    public AppInfoMapper getAppInfoMapper() {
        return appInfoMapper;
    }

    public void setAppInfoMapper(AppInfoMapper appInfoMapper) {
        this.appInfoMapper = appInfoMapper;
    }

    @Override
    public int add(AppInfo appInfo) {
        return appInfoMapper.add(appInfo);
    }

    @Override
    public int deleteAppInfoById(Integer id) {
        return appInfoMapper.deleteAppInfoById(id);
    }

    @Override
    public int update(AppInfo appInfo) {
        return appInfoMapper.update(appInfo);
    }

    @Override
    public int getAppInfoCount(Map<String, Object> map) {
        return appInfoMapper.getAppInfoCount(map);
    }

    @Override
    public AppInfo getAppInfoById(Integer id) {
        return appInfoMapper.getAppInfoById(id);
    }

    @Override
    public List<AppInfo> getAppInfoListByObj(AppInfo appInfo) {
        return appInfoMapper.getAppInfoListByObj(appInfo);
    }

    @Override
    public List<AppInfo> getAppInfoPageByMap(Map<String, Object> map) {
        return appInfoMapper.getAppInfoPageByMap(map);
    }

    @Override
    public AppInfo getAppInfoByMap(Map<String, Object> map) {
        return appInfoMapper.getAppInfoByMap(map);
    }
}
