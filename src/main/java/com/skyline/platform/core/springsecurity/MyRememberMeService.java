package com.skyline.platform.core.springsecurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyRememberMeService extends PersistentTokenBasedRememberMeServices {
	private MyJdbcTokenRepositoryImpl tokenRepository;
	
	public MyRememberMeService(String key, UserDetailsService userDetailsService,
			PersistentTokenRepository tokenRepository) {
		super(key, userDetailsService, tokenRepository);
		this.tokenRepository = (MyJdbcTokenRepositoryImpl) tokenRepository;
	}
	
	@Override
	protected void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication successfulAuthentication) {
		String username = successfulAuthentication.getName();
		tokenRepository.removeUserTokens(username);
		super.onLoginSuccess(request, response, successfulAuthentication);
	}
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String rememberMeCookie = extractRememberMeCookie(request);
		if (rememberMeCookie == null) {
			return ;
		}
		logger.debug("Remember-me cookie detected");
		if (rememberMeCookie.length() == 0) {
			logger.debug("Cookie was empty");
			cancelCookie(request, response);
			return ;
		}
		cancelCookie(request, response);
		String[] cookieTokens = decodeCookie(rememberMeCookie);
		final String presentedSeries = cookieTokens[0];
		if (authentication != null) {
			tokenRepository.removeSeriesTokens(presentedSeries);
		}
	}

}
