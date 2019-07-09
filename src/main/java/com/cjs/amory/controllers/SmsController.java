package com.cjs.amory.controllers;

import com.cjs.amory.apis.ApiConstants;
import com.cjs.amory.common.CommonResult;
import com.cjs.amory.common.ResultCode;
import com.cjs.amory.domains.SendCodeRequest;
import com.cjs.amory.services.SmsService;
import com.cjs.amory.utils.PhoneUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * aliyun sms service
 *
 * @Author: amory
 * @Date: 2019-07-09
 */
@RestController
@Api (tags = "SmsController", description = "Sms管理")
@RequestMapping (ApiConstants.SMS)
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 发送验证码
     *
     * @param sendCodeRequest
     */
    @RequestMapping(value = ApiConstants.SEND_CODE, method = POST)
    @ApiOperation ("发送验证码操作")
    @ApiModelProperty (value="phoneNum",notes = "String")
    public CommonResult sendCode(@RequestBody final SendCodeRequest sendCodeRequest) {
        /**
         * 检测必要的参数
         */
        if (!PhoneUtil.isValidPhoneNum(sendCodeRequest.getPhoneNum())){
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        }
        log.info("in sendCode-phone.number:{}", sendCodeRequest.getPhoneNum());
        return smsService.sendCode(sendCodeRequest.getPhoneNum());
    }
}
