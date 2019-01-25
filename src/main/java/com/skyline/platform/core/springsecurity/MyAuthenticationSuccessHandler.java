package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.platform.core.model.ResponseStatus;
import com.skyline.util.NetworkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	SessionRegistry sessionRegistry;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {

		request.getSession().removeAttribute("verifyCode");
		request.getSession().removeAttribute("verifyTime");

		ResponseModel result = new ResponseModel(ResponseStatus.LOGIN_SUCCESS);
		
		List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
		String currentSessionId = request.getSession(false).getId();
		
		if(sessions.size() > 1) {
			result = new ResponseModel(ResponseStatus.LOGIN_USER_ALREADY_LOGGED);
			for (SessionInformation sessionInfo : sessions) {
				if(!sessionInfo.getSessionId().equals(currentSessionId)) {
					((MySessionInformation)sessionInfo).setKicked();
					sessionInfo.expireNow();
					if (sessionRegistry instanceof MySessionRegistryImpl) {
						((MySessionRegistryImpl) sessionRegistry).updateSessionInformation(sessionInfo);
					}
				}
			}
		}
		
		NetworkUtil.writeToResponse(response, result);
	}
}
