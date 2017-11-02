package com.app.service;

import com.app.pojo.AppCategory;

import java.util.List;
import java.util.Map;

public interface AppCategoryService {
    /**
     * add
     * @param appCategory
     * @return
     */
    int add(AppCategory appCategory);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteAppCategoryById(Integer id);

    /**
     * update
     * @param appCategory
     * @return
     */
    int update(AppCategory appCategory);

    /**
     * getCount
     * @param map
     * @return
     */
    int getAppCategoryCount(Map<String, Object> map);

    /**
     * getObjById
     * @param id
     * @return
     */
    AppCategory getAppCategoryById(Integer id);

    /**
     * getObjByMap
     * @param map
     * @return
     */
    AppCategory getAppCategoryByMap(Map<String, Object> map);

    /**
     * getListByObj
     * @return
     */
    List<AppCategory> getAppCategoryListByObj(AppCategory appCategory);

    /**
     * getPageByMap
     * @param map
     * @return
     */
    List<AppCategory> getAppCategoryPageByMap(Map<String, Object> map);
}
