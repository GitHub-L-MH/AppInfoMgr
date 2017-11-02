package com.app.service;

import com.app.pojo.AppInfo;

import java.util.List;
import java.util.Map;

public interface AppInfoService {
    /**
     * add
     * @param appInfo
     * @return
     */
    int add(AppInfo appInfo);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteAppInfoById(Integer id);

    /**
     * update
     * @param appInfo
     * @return
     */
    int update(AppInfo appInfo);

    /**
     * getCount
     * @param map
     * @return
     */
    int getAppInfoCount(Map<String, Object> map);

    /**
     * getObjById
     * @param id
     * @return
     */
    AppInfo getAppInfoById(Integer id);

    /**
     * getObjByMap
     * @param map
     * @return
     */
    AppInfo getAppInfoByMap(Map<String, Object> map);

    /**
     * getListByObj
     * @return
     */
    List<AppInfo> getAppInfoListByObj(AppInfo appInfo);

    /**
     * getPageByMap
     * @param map
     * @return
     */
    List<AppInfo> getAppInfoPageByMap(Map<String, Object> map);
}
