package com.app.service.impl;

import com.app.mapper.AppCategoryMapper;
import com.app.pojo.AppCategory;
import com.app.service.AppCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("appCategoryServiceImpl")
public class AppCategoryServiceImpl implements AppCategoryService {
    @Autowired(required = false)
    private AppCategoryMapper appCategoryMapper;

    public AppCategoryMapper getAppCategoryMapper() {
        return appCategoryMapper;
    }

    public void setAppCategoryMapper(AppCategoryMapper appCategoryMapper) {
        this.appCategoryMapper = appCategoryMapper;
    }

    @Override
    public int add(AppCategory appCategory) {
        return appCategoryMapper.add(appCategory);
    }

    @Override
    public int deleteAppCategoryById(Integer id) {
        return appCategoryMapper.deleteAppCategoryById(id);
    }

    @Override
    public int update(AppCategory appCategory) {
        return appCategoryMapper.update(appCategory);
    }

    @Override
    public int getAppCategoryCount(Map<String, Object> map) {
        return appCategoryMapper.getAppCategoryCount(map);
    }

    @Override
    public AppCategory getAppCategoryById(Integer id) {
        return appCategoryMapper.getAppCategoryById(id);
    }

    @Override
    public List<AppCategory> getAppCategoryListByObj(AppCategory appCategory) {
        return appCategoryMapper.getAppCategoryListByObj(appCategory);
    }

    @Override
    public List<AppCategory> getAppCategoryPageByMap(Map<String, Object> map) {
        return appCategoryMapper.getAppCategoryPageByMap(map);
    }

    @Override
    public AppCategory getAppCategoryByMap(Map<String, Object> map) {
        return appCategoryMapper.getAppCategoryByMap(map);
    }
}
