package com.app.service;

import com.app.pojo.AppVersion;

import java.util.List;
import java.util.Map;

public interface AppVersionService {
    /**
     * add
     * @param appVersion
     * @return
     */
    int add(AppVersion appVersion);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteAppVersionById(Integer id);

    /**
     * update
     * @param appVersion
     * @return
     */
    int update(AppVersion appVersion);

    /**
     * getCount
     * @param map
     * @return
     */
    int getAppVersionCount(Map<String, Object> map);

    /**
     * getObjById
     * @param id
     * @return
     */
    AppVersion getAppVersionById(Integer id);

    /**
     * getObjByMap
     * @param map
     * @return
     */
    AppVersion getAppVersionByMap(Map<String, Object> map);

    /**
     * getListByObj
     * @return
     */
    List<AppVersion> getAppVersionListByObj(AppVersion appVersion);

    /**
     * getPageByMap
     * @param map
     * @return
     */
    List<AppVersion> getAppVersionPageByMap(Map<String, Object> map);
}
