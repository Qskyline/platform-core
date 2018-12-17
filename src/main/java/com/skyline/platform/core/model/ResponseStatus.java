package com.skyline.platform.core.model;

/**
 * @ClassName ResponseStatus
 * @Description TODO
 * @Author skyline
 * @Date 2018/12/17 17:51
 * Version 1.0
 **/
public enum ResponseStatus {
    REGISTER_SUCCESS("Register Success.", 0),
    REGISTER_FAILED_U_E("The user name is already exit.", 1),
    REGISTER_FAILED_M_E("The mobilephone number is already registered.", 2),
    REGISTER_FAILED_UNKNOWN_ERROR("Unknow Error.", 3),
    REGISTER_FAILED_SECURITYCHECK("Security Check failed.", 4),
    LOGIN_SUCCESS("Login Success.", 100),
    LOGIN_USER_ALREADY_LOGGED("The user has logged somewhere else. Cause the single-user restriction, the previous user has been nick out, and the current user will login.", 101),
    LOGIN_AUTH_FAILED("Authentication Failed.", 102),
    LOGIN_USER_LOCKED("The current user has been locked.", 103),
    LOGIN_USER_NICKOUT_ERROR("The user current status is abnormal. Login Forbidden.", 104),
    LOGIN_SECURITY_SQLINJECTION("Do you want injection? To be a good person. Go away please!", 105),
    LOGIN_VERIFYCODE_ERROR("The VerifyCode is error!", 106),
    LOGIN_VERIFYCODE_TIMEOUT("The VerifyCode is timeout!", 107),
    LOGIN_UNKNOWN_ERROR("Unknown Error!", 108),
    STATUS_LOGGED("The current user is already logged.", 200),
    STATUS_NOLOGGED("The current user has not been logged.", 201),
    STATUS_SESSION_TIMEOUT("The current session time out. Please login again.", 202),
    STATUS_SESSION_SINGLE_USER_RESTRICTION("The current user has been nicked out.", 203),
    LOGOUT_SUCCESS("Logout Success.", 300),
    OPERATION_ERROR("Operation Failed.", 400),
    OPERATION_ERROR_PARAMS("params Error.", 401),
    OPERATION_ERROR_ACCESS_DENY("Access Deny.", 402);

    private String msg;
    private int code;

    ResponseStatus(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
