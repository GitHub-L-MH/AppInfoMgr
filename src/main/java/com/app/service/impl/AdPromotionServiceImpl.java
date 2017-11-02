package com.app.service.impl;

import com.app.mapper.AdPromotionMapper;
import com.app.pojo.AdPromotion;
import com.app.service.AdPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("adPromotionServiceImpl")
public class AdPromotionServiceImpl implements AdPromotionService {
    @Autowired(required = false)
    private AdPromotionMapper adPromotionMapper;

    public AdPromotionMapper getAdPromotionMapper() {
        return adPromotionMapper;
    }

    public void setAdPromotionMapper(AdPromotionMapper adPromotionMapper) {
        this.adPromotionMapper = adPromotionMapper;
    }

    @Override
    public int add(AdPromotion adPromotion) {
        return adPromotionMapper.add(adPromotion);
    }

    @Override
    public int deleteAdPromotionById(Integer id) {
        return adPromotionMapper.deleteAdPromotionById(id);
    }

    @Override
    public int update(AdPromotion adPromotion) {
        return adPromotionMapper.update(adPromotion);
    }

    @Override
    public int getAdPromotionCount(Map<String, Object> map) {
        return adPromotionMapper.getAdPromotionCount(map);
    }

    @Override
    public AdPromotion getAdPromotionById(Integer id) {
        return adPromotionMapper.getAdPromotionById(id);
    }

    @Override
    public List<AdPromotion> getAdPromotionListByObj(AdPromotion adPromotion) {
        return adPromotionMapper.getAdPromotionListByObj(adPromotion);
    }

    @Override
    public List<AdPromotion> getAdPromotionPageByMap(Map<String, Object> map) {
        return adPromotionMapper.getAdPromotionPageByMap(map);
    }

    @Override
    public AdPromotion getAdPromotionByMap(Map<String, Object> map) {
        return adPromotionMapper.getAdPromotionByMap(map);
    }
}
