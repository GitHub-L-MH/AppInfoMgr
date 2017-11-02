package com.app.service.impl;

import com.app.mapper.AppVersionMapper;
import com.app.pojo.AppVersion;
import com.app.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("appVersionServiceImpl")
public class AppVersionServiceImpl implements AppVersionService {
    @Autowired(required = false)
    private AppVersionMapper appVersionMapper;

    public AppVersionMapper getAppVersionMapper() {
        return appVersionMapper;
    }

    public void setAppVersionMapper(AppVersionMapper appVersionMapper) {
        this.appVersionMapper = appVersionMapper;
    }

    @Override
    public int add(AppVersion appVersion) {
        return appVersionMapper.add(appVersion);
    }

    @Override
    public int deleteAppVersionById(Integer id) {
        return appVersionMapper.deleteAppVersionById(id);
    }

    @Override
    public int update(AppVersion appVersion) {
        return appVersionMapper.update(appVersion);
    }

    @Override
    public int getAppVersionCount(Map<String, Object> map) {
        return appVersionMapper.getAppVersionCount(map);
    }

    @Override
    public AppVersion getAppVersionById(Integer id) {
        return appVersionMapper.getAppVersionById(id);
    }

    @Override
    public List<AppVersion> getAppVersionListByObj(AppVersion appVersion) {
        return appVersionMapper.getAppVersionListByObj(appVersion);
    }

    @Override
    public List<AppVersion> getAppVersionPageByMap(Map<String, Object> map) {
        return appVersionMapper.getAppVersionPageByMap(map);
    }

    @Override
    public AppVersion getAppVersionByMap(Map<String, Object> map) {
        return appVersionMapper.getAppVersionByMap(map);
    }
}
