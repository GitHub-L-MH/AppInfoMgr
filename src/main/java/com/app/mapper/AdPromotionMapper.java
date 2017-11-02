package com.app.mapper;

import com.app.pojo.AdPromotion;

import java.util.List;
import java.util.Map;

public interface AdPromotionMapper {
    /**
     * add
     * @param adPromotion
     * @return
     */
    int add(AdPromotion adPromotion);

    /**
     * delete by id
     * @param id
     * @return
     */
    int deleteAdPromotionById(Integer id);

    /**
     * update
     * @param adPromotion
     * @return
     */
    int update(AdPromotion adPromotion);

    /**
     * getCount
     * @param map
     * @return
     */
    int getAdPromotionCount(Map<String,Object> map);

    /**
     * getObjById
     * @param id
     * @return
     */
    AdPromotion getAdPromotionById(Integer id);

    /**
     * getObjByMap
     * @param map
     * @return
     */
    AdPromotion getAdPromotionByMap(Map<String,Object> map);

    /**
     * getListByObj
     * @param adPromotion
     * @return
     */
    List<AdPromotion> getAdPromotionListByObj(AdPromotion adPromotion);

    /**
     * getPageByMap
     * @param map
     * @return
     */
    List<AdPromotion> getAdPromotionPageByMap(Map<String,Object> map);
}
