package com.app.vo;

import java.io.Serializable;

/**
 * 数据基类
 */
public class BaseResult implements Serializable {

    private String status;
    private String message;
    private Object data;
    private String errorCode;

    public BaseResult() {
    }

    public BaseResult(String status, String message, Object data, String errorCode) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
