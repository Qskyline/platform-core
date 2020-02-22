package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.common.enums.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    MyUserDetails myUserDetails;

    @Autowired
    WeChatUserDetails weChatUserDetails;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public MyAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) authentication.getDetails();
        if (details.isActive()) {
            String imageCode = details.getImageCode();
            String session_imageCode = details.getSession_imageCode();
            long session_imageTime = details.getSession_imageTime();

            if (imageCode == null || session_imageCode == null) {
                throw new MyAuthenticationException("VerifyCode Error.", 1);
            }

            if (!imageCode.toLowerCase().equals(session_imageCode.toLowerCase())) {
                throw new MyAuthenticationException("VerifyCode Error.", 1);
            } else {
                long nowTime = System.currentTimeMillis();
                if ((nowTime - session_imageTime) / 1000 > 60) { //大于60s,超时
                    throw new MyAuthenticationException("VerifyCode Timeout.", 2);
                }
            }
        }

        if (LoginType.weChat.equals(details.getLoginType())) {
            this.setUserDetailsService(weChatUserDetails);
        } else {
            this.setUserDetailsService(myUserDetails);
        }
        this.setPasswordEncoder(bCryptPasswordEncoder);

        return super.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    protected void doAfterPropertiesSet() {
        this.setUserDetailsService(myUserDetails);
        this.setPasswordEncoder(bCryptPasswordEncoder);
    }
}
