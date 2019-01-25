package com.skyline.platform.core.springsecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MyRememberMeFilter extends RememberMeAuthenticationFilter{
	private SessionRegistry sessionRegistry;
	
	public MyRememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices, SessionRegistry sessionRegistry) {
		super(authenticationManager, rememberMeServices);
		this.sessionRegistry = sessionRegistry;
	}
	
	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {

		List<SessionInformation> sessions = sessionRegistry.getAllSessions(authResult.getPrincipal(), false);
		HttpSession currentSession = request.getSession(false);
		String currentSessionId;
		if(currentSession != null) currentSessionId = currentSession.getId();
		else currentSessionId = request.getSession().getId();
		sessionRegistry.registerNewSession(currentSessionId, authResult.getPrincipal());
		if(sessions.size() >= 1) {
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
	}
}
