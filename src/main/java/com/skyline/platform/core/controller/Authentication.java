package com.skyline.platform.core.controller;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.platform.core.service.UserService;
import com.skyline.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class Authentication {
	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@RequestMapping(value = "/security/register.do", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
	public ResponseModel register(
			HttpServletResponse response,
			@RequestParam("name") String username,
			@RequestParam("mobile") String mobile,
			@RequestParam("password") String password,
			@RequestParam("confirm") String confirm
			){
		if(username ==null || mobile == null || password == null || confirm == null ||
				!SecurityUtil.checkUsername(username) || !SecurityUtil.checkPhoneNum(mobile) ||
				!password.equals(confirm)) {
			return new ResponseModel(ResponseModel.Status.REGISTER_FAILED_SECURITYCHECK);
		}
		
		UserService.RegisterStatus registerStatus = userService.register(username, mobile, bcryptEncoder.encode(password), null);
		ResponseModel responseModel;
		switch (registerStatus) {
		case success:
			responseModel = new ResponseModel(ResponseModel.Status.REGISTER_SUCCESS);
			break;
		case usernameAlreadyExist:
			responseModel = new ResponseModel(ResponseModel.Status.REGISTER_FAILED_U_E);
			break;
		case mobilephoneNumberAlreadyExist:
			responseModel = new ResponseModel(ResponseModel.Status.REGISTER_FAILED_M_E);
			break;
		case unknowError:
		default:
			responseModel = new ResponseModel(ResponseModel.Status.REGISTER_FAILED_UNKNOWN_ERROR);
		}
		return responseModel;
	}
	
	@RequestMapping(value = "/security/check_loginStatus.do", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
	public ResponseModel checkLoginStatus() {
		return new ResponseModel(ResponseModel.Status.STATUS_LOGGED);
	}

	@RequestMapping(value = "/security/flushVerifyCode.do", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
	public ResponseModel flushVerifyCode(HttpServletRequest request) {
		ResponseModel responseModel = new ResponseModel(ResponseModel.Status.STATUS_NOLOGGED);
		try {
			userService.addVerifyCode(request, responseModel);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseModel;
	}

	@RequestMapping(value = "/security/hasRole.do", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
	public ResponseModel hasRole(@RequestParam("role") String roleName) {
		if (userService.hasRole(roleName)) return new ResponseModel("true");
		else return new ResponseModel("false");
	}
}
