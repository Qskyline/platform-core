package com.skyline.platform.core.springsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyClearLogoutHandler implements LogoutHandler {
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        applicationContext.publishEvent(new HttpSessionDestroyedEvent(request.getSession(false)));
    }
}
