package com.skyline.platform.core.springsecurity;

import com.skyline.platform.core.entity.User;
import com.skyline.platform.core.entity.UserRole;
import com.skyline.platform.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetails implements UserDetailsService{
	@Autowired
	private UserService userService;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.getUser(username);
		if(user == null || user.getIsFrozen()) {
			throw new UsernameNotFoundException("Username not found");
		}
		if(user.getIsLocked()) {
			userService.attemptUnlockUser(user, 10);
		}
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				true,
				true, 
				true, 
				!user.getIsLocked(),
				getGrantedAuthorities(user.getUserRole())
				);
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<UserRole> l_u){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (UserRole userRole : l_u) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName().toUpperCase()));
		}
		return authorities;  
	}
}