package com.skyline.platform.core.controller;

import com.skyline.platform.core.model.ResponseModel;

public class BaseController {
    public ResponseModel doIt(boolean isSuccess) {
        ResponseModel responseModel;
        if (isSuccess) responseModel = new ResponseModel("success");
        else responseModel = new ResponseModel(ResponseModel.Status.OPERATION_FAILED);
        return responseModel;
    }
}
