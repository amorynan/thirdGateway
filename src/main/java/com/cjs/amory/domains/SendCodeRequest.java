package com.cjs.amory.domains;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: amory
 * @Date: 2019-07-09
 */
@ApiModel ("sendCodeRequest")
@Data
public class SendCodeRequest {

    @ApiModelProperty (value = "phoneNum", dataType = "string", required = true)
    private String phoneNum;
}
