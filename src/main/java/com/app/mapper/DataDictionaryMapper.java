package com.app.mapper;

import com.app.pojo.DataDictionary;

import java.util.List;
import java.util.Map;

public interface DataDictionaryMapper {
    /**
     * add
     * @param dataDictionary
     * @return
     */
    int add(DataDictionary dataDictionary);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteDataDictionaryById(Integer id);

    /**
     * update
     * @param dataDictionary
     * @return
     */
    int update(DataDictionary dataDictionary);

    /**
     * getCount
     * @param map
     * @return
     */
    int getDataDictionaryCount(Map<String,Object> map);

    /**
     * getObjById
     * @param id
     * @return
     */
    DataDictionary getDataDictionaryById(Integer id);

    /**
     * getObjByMap
     * @param map
     * @return
     */
    DataDictionary getDataDictionaryByMap(Map<String,Object> map);

    /**
     * getListByObj
     * @param dataDictionary
     * @return
     */
    List<DataDictionary> getDataDictionaryListByObj(DataDictionary dataDictionary);

    /**
     * getPageByMap
     * @param map
     * @return
     */
    List<DataDictionary> getDataDictionaryPageByMap(Map<String,Object> map);

}
