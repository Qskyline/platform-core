package com.skyline.platform.core.common;

import com.skyline.platform.core.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName util
 * @Description TODO
 * @Author skyline
 * @Date 2020/2/16 17:21
 * Version 1.0
 **/
public class Util {
    public static List<GrantedAuthority> getGrantedAuthorities(List<UserRole> l_u){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (UserRole userRole : l_u) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName().toUpperCase()));
        }
        return authorities;
    }
}
