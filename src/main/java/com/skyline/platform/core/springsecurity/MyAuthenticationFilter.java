package com.skyline.platform.core.springsecurity;

import com.skyline.util.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public MyAuthenticationFilter(String pattern, String httpMethod) {
		super(new AntPathRequestMatcher(pattern, httpMethod));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse arg1) throws AuthenticationException {
		String username = request.getParameter("name");
		String password = request.getParameter("password");
		if(!SecurityUtil.checkUsername(username)) {
			throw new MyAuthenticationException("Username format check failed!", 0);
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		authRequest.setDetails(new MyWebAuthenticationDetails(request));
		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
