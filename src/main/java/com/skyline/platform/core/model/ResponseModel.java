package com.skyline.platform.core.model;

import com.skyline.platform.core.common.enums.ResponseStatus;

public class ResponseModel {
    private String errMsg;
    private int statusCode;
    private Object data;

    public ResponseModel(Object data) {
        this.statusCode = ResponseStatus.STATUS_LOGGED.getCode();
        this.errMsg = ResponseStatus.STATUS_LOGGED.getMsg();
        this.data = data;
    }

    public ResponseModel(ResponseStatus status) {
        this.statusCode = status.getCode();
        this.errMsg = status.getMsg();
        this.data = null;
    }

    public ResponseModel(ResponseStatus status, String errMsg) {
        this.statusCode = status.getCode();
        this.errMsg = errMsg;
        this.data = null;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
