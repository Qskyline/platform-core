package com.skyline.platform.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home extends BaseController {
	@RequestMapping(value = "/security/getHomePageData.do", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	public Object getHomePageData() {
		return doIt(true);
	}
}
