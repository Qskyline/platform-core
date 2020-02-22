package com.skyline.platform.core.springsecurity;

import com.alibaba.fastjson.JSONObject;
import com.skyline.util.StringUtil;
import org.apache.velocity.runtime.parser.CharStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	Logger logger = LoggerFactory.getLogger(MyAuthenticationFilter.class);

	public MyAuthenticationFilter(String pattern, String httpMethod) {
		super(new AntPathRequestMatcher(pattern, httpMethod));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse arg1) throws AuthenticationException {
		JSONObject params;
		try {
			BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}
			params = JSONObject.parseObject(responseStrBuilder.toString());
		} catch (IOException e) {
			logger.error(StringUtil.getExceptionStackTraceMessage(e));
			throw new UsernameNotFoundException("We need the user info.");
		}

		String username = params.getString("name");
		String password = params.getString("password");
		/*if(!SecurityUtil.checkUsername(username)) {
			throw new MyAuthenticationException("Username format check failed!", 0);
		}*/
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		authRequest.setDetails(new MyWebAuthenticationDetails(params, request));
		return this.getAuthenticationManager().authenticate(authRequest);
	}
}
