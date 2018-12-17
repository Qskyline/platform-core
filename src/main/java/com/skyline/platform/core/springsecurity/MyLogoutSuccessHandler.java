package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.platform.core.model.ResponseStatus;
import com.skyline.util.NetworkUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse response, Authentication paramAuthentication)
			throws IOException {
		NetworkUtil.writeToResponse(response, new ResponseModel(ResponseStatus.LOGOUT_SUCCESS));
	}
}
