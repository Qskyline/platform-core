package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {	

	@Autowired
	private MySessionRegistryImpl mySessionRegistryImpl;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {

		request.getSession().removeAttribute("verifyCode");
		request.getSession().removeAttribute("verifyTime");

		ResponseModel result = new ResponseModel(ResponseModel.Status.LOGIN_SUCCESS);
		
		List<SessionInformation> sessions = mySessionRegistryImpl.getAllSessions(authentication.getPrincipal(), false);
		String currentSessionId = request.getSession(false).getId();
		
		if(sessions.size() > 1) {
			result = new ResponseModel(ResponseModel.Status.LOGIN_USER_ALREADY_LOGGED);
			for (SessionInformation sessionInfo : sessions) {
				if(!sessionInfo.getSessionId().equals(currentSessionId)) {
					((MySessionInformation)sessionInfo).setKicked();
					sessionInfo.expireNow();
					mySessionRegistryImpl.updateSessionInformation(sessionInfo);
				}
			}
		}
		
		NetworkUtil.writeToResponse(response, result);
	}
}
