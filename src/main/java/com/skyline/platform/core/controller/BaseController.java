package com.skyline.platform.core.controller;

import com.skyline.platform.core.model.ResponseModel;
import com.skyline.platform.core.common.enums.ResponseStatus;

public class BaseController {
    public ResponseModel doIt(boolean isSuccess, Object data) {
        ResponseModel responseModel;
        if (isSuccess) {
            responseModel = new ResponseModel(data);
        }
        else if (data instanceof String) {
            responseModel = new ResponseModel(ResponseStatus.OPERATION_ERROR, (String) data);
        } else {
            responseModel = new ResponseModel(ResponseStatus.OPERATION_ERROR);
        }
        return responseModel;
    }

    public ResponseModel doIt(boolean isSuccess) {
        ResponseModel responseModel;
        if (isSuccess) responseModel = new ResponseModel("success");
        else responseModel = new ResponseModel(ResponseStatus.OPERATION_ERROR);
        return responseModel;
    }
}
