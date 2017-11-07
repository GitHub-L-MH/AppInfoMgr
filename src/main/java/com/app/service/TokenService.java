package com.app.service;

import com.app.pojo.BackendUser;

/**
 * Token端口
 */
public interface TokenService {
    /**
     * 会话有效时间
     */
    public final  static int SESSION_TIMEOUT = 2*60*60;

    /**
     * 创建token字符串
     * @param userAgent
     * @param backendUser
     * @return
     */
    public String generateToken(String userAgent, BackendUser backendUser);

    /**
     * 保存到redis中
     * @param token
     * @param backendUser
     */
    public void saveToken(String token, BackendUser backendUser);

    /**
     * 获取 User
     * @param token
     * @return
     */
    public BackendUser load(String token);

}
