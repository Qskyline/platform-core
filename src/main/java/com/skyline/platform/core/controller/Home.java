package com.skyline.platform.core.controller;

import com.skyline.platform.core.model.ResponseModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {
	@RequestMapping(value = "/security/getHomePageData.do", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	public Object getHomePageData() {
		return doIt(true);
	}

	private ResponseModel doIt(boolean isSuccess) {
		ResponseModel responseModel;
		if (isSuccess) responseModel = new ResponseModel("success");
		else responseModel = new ResponseModel(ResponseModel.Status.OPERATION_FAILED);
		return responseModel;
	}
}
