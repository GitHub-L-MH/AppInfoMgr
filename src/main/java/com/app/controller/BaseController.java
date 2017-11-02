package com.app.controller;

import com.app.vo.BaseResult;
import com.app.vo.HTTPStatus;

/**
 * 控制器基类
 */
public class BaseController {

    /**
     * 构造成功返回信息
     * @param object
     * @return
     */
    public BaseResult buildSuccessResultInfo(Object object){
        BaseResult vo = new BaseResult();
        vo.setStatus(HTTPStatus.OK);
        vo.setMessage("success");
        vo.setData(object);
        return vo;
    }

    /**
     * 构造失败的返回结果
     * @param status 状态码
     * @param message 消息
     * @param errorCode 错误编码
     * @return
     */
    public BaseResult buildFailedResultInfo(String status,String message,String errorCode){
        BaseResult vo = new BaseResult();
        vo.setStatus(status);
        vo.setMessage(message);
        vo.setData(null);
        vo.setErrorCode(errorCode);
        return vo;
    }
}
