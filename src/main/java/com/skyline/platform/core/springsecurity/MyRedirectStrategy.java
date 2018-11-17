package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.util.NetworkUtil;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyRedirectStrategy implements RedirectStrategy{
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response,
			String paramString) throws IOException {
		NetworkUtil.writeToResponse(response, new ResponseModel(ResponseModel.Status.STATUS_SESSION_TIMEOUT));
	}
}
