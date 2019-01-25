package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MyAuthenticationSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	@Autowired
    UserService userService;
	@Autowired
	private SessionRegistry sessionRegistry;
	
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent paramE) {
		Authentication auth = paramE.getAuthentication();
		String userName = auth.getName();
		if(!userService.updateSuccessUserLoginlog(userName)) {
			//登录日志记录出错,强制下线
			List<SessionInformation> sessions = sessionRegistry.getAllSessions(auth.getPrincipal(), false);
			for (SessionInformation sessionInfo : sessions) {
				sessionInfo.expireNow();
				if (sessionRegistry instanceof MySessionRegistryImpl) {
					((MySessionRegistryImpl) sessionRegistry).updateSessionInformation(sessionInfo);
				}
			}
		}
	}
}
