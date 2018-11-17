package com.skyline.platform.core.springsecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MyRememberMeFilter extends RememberMeAuthenticationFilter{
	private MySessionRegistryImpl mySessionRegistryImpl;
	
	public MyRememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices, MySessionRegistryImpl mySessionRegistry) {
		super(authenticationManager, rememberMeServices);
		this.mySessionRegistryImpl = mySessionRegistry;
	}
	
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {

		List<SessionInformation> sessions = mySessionRegistryImpl.getAllSessions(authResult.getPrincipal(), false);
		HttpSession currentSession = request.getSession(false);
		String currentSessionId;
		if(currentSession != null) currentSessionId = currentSession.getId();
		else currentSessionId = request.getSession().getId();
		mySessionRegistryImpl.registerNewSession(currentSessionId, authResult.getPrincipal());
		if(sessions.size() >= 1) {
			for (SessionInformation sessionInfo : sessions) {
				if(!sessionInfo.getSessionId().equals(currentSessionId)) {
					((MySessionInformation)sessionInfo).setKicked();
					sessionInfo.expireNow();
					mySessionRegistryImpl.updateSessionInformation(sessionInfo);
				}
			}
		}
	}
}
