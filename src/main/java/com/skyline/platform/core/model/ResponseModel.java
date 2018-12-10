package com.skyline.platform.core.model;

public class ResponseModel {
	private String errMsg;
	private int statusCode;
	private Object data;
	
	public enum Status{
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
		STATUS_ACCESS_DENY("Access Deny.", 204),
		LOGOUT_SUCCESS("Logout Success.", 300),
		OPERATION_FAILED("operation failed", 400);

		private String msg;
		private int code;

		Status(String msg, int code) {
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
	
	public ResponseModel(Object data) {
		this.statusCode = Status.STATUS_LOGGED.getCode();
		this.errMsg = Status.STATUS_LOGGED.getMsg();
		this.data = data;
	}
	
	public ResponseModel(Status status) {
		this.statusCode = status.getCode();
		this.errMsg = status.getMsg();
		this.data = null;
	}

	public ResponseModel(Status status, String errMsg) {
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
