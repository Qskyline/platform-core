package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.common.Util;
import com.skyline.platform.core.entity.User;
import com.skyline.platform.core.service.UserService;
import com.skyline.util.NetworkUtil;
import com.skyline.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @ClassName WeChatUserDetails
 * @Description TODO
 * @Author skyline
 * @Date 2019/10/16 00:01
 * Version 1.0
 **/

@Component
public class WeChatUserDetails implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(WeChatUserDetails.class);

    @Autowired
    Environment environment;

    @Autowired
    UserService userService;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        String appId = environment.getProperty("weChatAppId");
        String appSecret = environment.getProperty("weChatAppSecret");
        String weChatLoginUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", appId, appSecret, name);

        String data;
        try {
            data = NetworkUtil.sendGet(weChatLoginUrl, null);
        } catch (Exception e) {
            logger.error(StringUtil.getExceptionStackTraceMessage(e));
            throw new UsernameNotFoundException("Fetch user info from WeiXin open API failed: " + e.getMessage());
        }

        JSONObject jsonObject = JSONObject.fromObject(data);
        String openid = (String) jsonObject.get("openid");
        /*String session_key = jsonObject.getString("session_key");
        String unionid = jsonObject.getString("unionid");*/

        if (StringUtils.isEmpty(openid)) {
            throw new UsernameNotFoundException("Fetch user info from WeiXin open API failed: " + jsonObject.get("errmsg"));
        }

        User user = userService.getUser(openid);
        if (user == null) {
            UserService.RegisterStatus registerStatus = userService.register(openid, "", "", "", true);
            if (!registerStatus.equals(UserService.RegisterStatus.success)) {
                logger.error(registerStatus.toString());
                return null;
            }
        }
        user = userService.getUser(openid);

        return new org.springframework.security.core.userdetails.User(
                name,
                bcryptEncoder.encode("weChat"),
                true,
                true,
                true,
                true,
                Util.getGrantedAuthorities(user.getUserRole())
        );
    }
}
