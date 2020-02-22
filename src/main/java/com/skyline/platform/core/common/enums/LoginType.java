package com.skyline.platform.core.common.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName LoginType
 * @Description TODO
 * @Author skyline
 * @Date 2019/10/15 22:55
 * Version 1.0
 **/
public enum LoginType {
    unKnown(-1, "unKnown"),
    userAuth(0, "userAuth"),
    weChat(1, "weChat");

    private int loginCode;
    private String loginStr;

    LoginType(int loginCode, String loginStr) {
        this.loginCode = loginCode;
        this.loginStr = loginStr;
    }

    public static LoginType getByStr(String loginStr) {
        if (StringUtils.isBlank(loginStr)) {
            return unKnown;
        }
        for (LoginType loginType : LoginType.values()) {
            if (loginType.getLoginStr().equals(loginStr)) {
                return loginType;
            }
        }
        return unKnown;
    }

    public int getLoginCode() {
        return loginCode;
    }

    public String getLoginStr() {
        return loginStr;
    }
}
