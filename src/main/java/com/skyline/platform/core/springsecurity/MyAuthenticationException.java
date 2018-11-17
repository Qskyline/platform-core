package com.skyline.platform.core.springsecurity;

import org.springframework.security.core.AuthenticationException;

public class MyAuthenticationException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	/**
	 @value 0: formatError,
	 @value 1: verifyCodeError,
	 @value 2: verifyCodeTimeout
	 */
	private int type = 0;

	public MyAuthenticationException(String msg, int type) {
		super(msg);
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
