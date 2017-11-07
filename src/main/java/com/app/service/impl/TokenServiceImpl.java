package com.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.app.pojo.BackendUser;
import com.app.service.TokenService;
import com.app.tools.RedisAPI;
import com.app.tools.SecurityUtil;
import com.app.tools.UserAgentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired(required = false)
    private RedisAPI redisAPI;

    @Override
    public String generateToken(String userAgent, BackendUser backendUser) {
        //0，前缀
        StringBuilder tokenSb = new StringBuilder("token-");
        //1. 设备标识
        String device = UserAgentUtil.CheckAgent(userAgent);
        tokenSb.append(device + "-");
        //2. userCode加密
        tokenSb.append(SecurityUtil.md5Hex3(backendUser.getUserCode()) + "-");
        //3. id
        tokenSb.append(backendUser.getId() + "-");
        //4. createDate
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String createDate = sdf.format(new Date());
        tokenSb.append(createDate + "-");
        //5. 设备标识 userAgent 加密后，截取的前6为字符
        String random = SecurityUtil.md5Hex3(userAgent).substring(0, 6);
        tokenSb.append(random);
        return tokenSb.toString();
    }

    @Override
    public void saveToken(String token, BackendUser backendUser) {
        String jsonString = JSON.toJSONString(backendUser);
        redisAPI.set(token,TokenService.SESSION_TIMEOUT,jsonString);
    }

    @Override
    public BackendUser load(String token) {
        BackendUser backendUser = JSON.parseObject(redisAPI.get(token), BackendUser.class);
        return backendUser;
    }

}
