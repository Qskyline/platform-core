package com.skyline.platform.core.springsecurity;

import org.springframework.security.web.authentication.WebAuthenticationDetails;
import javax.servlet.http.HttpServletRequest;

public class MyWebAuthenticationDetails extends WebAuthenticationDetails {
    private String imageCode;
    private String session_imageCode;
    private long session_imageTime;
    private boolean isActive=false;

    public MyWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.imageCode = request.getParameter("verify");
        this.session_imageCode = (String)request.getSession().getAttribute("verifyCode");
        Object object = request.getSession().getAttribute("verifyTime");
        if (object == null) {
            this.session_imageTime = 0L;
        } else {
            this.session_imageTime = (long) object;
        }
        String count = (String) request.getSession().getAttribute("login_count");
        if (count != null && Integer.valueOf(count) >= 3) {
            isActive = true;
        }
    }

    public String getImageCode(){
        return imageCode;
    }

    public String getSession_imageCode() {
        return session_imageCode;
    }

    public long getSession_imageTime() {
        return session_imageTime;
    }

    public boolean isActive() {
        return isActive;
    }
}
