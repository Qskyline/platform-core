package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.platform.core.model.ResponseStatus;
import com.skyline.platform.core.service.UserService;
import com.skyline.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Autowired
    UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse response, AuthenticationException paramAuthenticationException)
			throws IOException {
		String count = (String) paramHttpServletRequest.getSession().getAttribute("login_count");
		if (count != null) {
			count = Integer.valueOf(count) + 1 + "";
		} else {
			count = "1";
		}
		paramHttpServletRequest.getSession().setAttribute("login_count", count);

		ResponseModel result;
		if(paramAuthenticationException instanceof LockedException) {
			result = new ResponseModel(ResponseStatus.LOGIN_USER_LOCKED);
		} else if(paramAuthenticationException instanceof SessionAuthenticationException) {
			result = new ResponseModel(ResponseStatus.LOGIN_USER_NICKOUT_ERROR);
		} else if(paramAuthenticationException instanceof MyAuthenticationException) {
			switch (((MyAuthenticationException) paramAuthenticationException).getType()) {
				case 0:
					result = new ResponseModel(ResponseStatus.LOGIN_SECURITY_SQLINJECTION);
					break;
				case 1:
					result = new ResponseModel(ResponseStatus.LOGIN_VERIFYCODE_ERROR);
					break;
				case 2:
					result = new ResponseModel(ResponseStatus.LOGIN_VERIFYCODE_TIMEOUT);
					break;
				default :
					result = new ResponseModel(ResponseStatus.LOGIN_UNKNOWN_ERROR);
			}
		} else {
			result = new ResponseModel(ResponseStatus.LOGIN_AUTH_FAILED);
		}

		userService.addVerifyCode(paramHttpServletRequest, result);

        NetworkUtil.writeToResponse(response, result);
	}

}
