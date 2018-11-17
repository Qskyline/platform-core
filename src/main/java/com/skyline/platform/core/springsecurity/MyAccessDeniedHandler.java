package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.util.NetworkUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2) throws IOException {
		NetworkUtil.writeToResponse(response, new ResponseModel(ResponseModel.Status.STATUS_ACCESS_DENY));
	}
}
