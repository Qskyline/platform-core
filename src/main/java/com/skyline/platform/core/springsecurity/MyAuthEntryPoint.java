package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.platform.core.model.ResponseStatus;
import com.skyline.platform.core.service.UserService;
import com.skyline.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthEntryPoint implements AuthenticationEntryPoint {
	@Autowired
    UserService userService;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		ResponseModel result = new ResponseModel(ResponseStatus.STATUS_NOLOGGED);
		userService.addVerifyCode(request, result);
		response.setContentType("application/json;charset=utf-8");
		NetworkUtil.writeToResponse(response, result);
	}
}
