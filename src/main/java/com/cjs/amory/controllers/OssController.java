package com.cjs.amory.controllers;

import com.cjs.amory.apis.ApiConstants;
import com.cjs.amory.common.CommonResult;
import com.cjs.amory.domains.OssCallbackResult;
import com.cjs.amory.domains.OssPolicyResult;
import com.cjs.amory.services.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: amory
 * @Date: 2019-07-09
 */
@RestController
@Api (tags = "OssController", description = "Oss管理")
@RequestMapping (ApiConstants.OSS)
public class OssController {
    @Autowired
    private OssService ossService;

    @ApiOperation (value = "oss上传签名生成")
    @RequestMapping(value = ApiConstants.POLICY, method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return CommonResult.success(result);
    }

    @ApiOperation(value = "oss上传成功回调")
    @RequestMapping(value = ApiConstants.CALLBACK, method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<OssCallbackResult> callback(HttpServletRequest request) {
        OssCallbackResult ossCallbackResult = ossService.callback(request);
        return CommonResult.success(ossCallbackResult);
    }
}
