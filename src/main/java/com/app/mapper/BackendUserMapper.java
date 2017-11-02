package com.app.mapper;

import com.app.pojo.BackendUser;

import java.util.List;
import java.util.Map;

public interface BackendUserMapper {
    /**
     * add
     * @param backendUser
     * @return
     */
    int add(BackendUser backendUser);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteBackendUserById(Integer id);

    /**
     * update
     * @param backendUser
     * @return
     */
    int update(BackendUser backendUser);

    /**
     * getCount
     * @param map
     * @return
     */
    int getBackendUserCount(Map<String,Object> map);

    /**
     * getObjById
     * @param id
     * @return
     */
    BackendUser getBackendUserById(Integer id);

    /**
     * getObjByMap
     * @param map
     * @return
     */
    BackendUser getBackendUserByMap(Map<String,Object> map);

    /**
     * getListByObj
     * @param backendUser
     * @return
     */
    List<BackendUser> getBackendUserListByObj(BackendUser backendUser);

    /**
     * getPageByMap
     * @param map
     * @return
     */
    List<BackendUser> getBackendUserPageByMap(Map<String,Object> map);

}
