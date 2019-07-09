package com.cjs.amory.services;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cjs.amory.common.CommonResult;
import com.cjs.amory.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: amory
 * @Date: 2019-07-09
 */
@Service
@Slf4j
public class SmsService {
    @Value ("${aliyun.product}")
    private String product;

    @Value("${aliyun.domain}")
    private String domain;

    @Value("${aliyun.accesskey.id}")
    private String accessKeyId;

    @Value("${aliyun.accesskey.secret}")
    private String accessKeySecret;

    @Value("${aliyun.signname}")
    private String signName;

    @Value("${spring.redis.prefix}")
    private String PRE_REDIS;

    @Value(("${spring.redis.timeout}"))
    private Long REDIS_EXPIRE_TIME;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 短信获取验证码
     * @param phoneNum
     */
    public CommonResult sendCode(final String phoneNum) {
        int code = 0;
        //检验redis 内是否有值并且是否过期
        if (stringRedisTemplate.opsForValue().get(PRE_REDIS+phoneNum) != null){
            return CommonResult.failed(ResultCode.UNAUTHORIZED);
        }
        setNewcode();
        try {
            code = getNewcode();
            if (sendSms(phoneNum, String.valueOf(getNewcode())).getCode().equals("OK")){
                stringRedisTemplate.opsForValue().set(PRE_REDIS+phoneNum, String.valueOf(code), REDIS_EXPIRE_TIME, TimeUnit.MILLISECONDS);
                log.info("put the {} and code {} into redis",phoneNum, code);
            }else{
                return CommonResult.failed(ResultCode.FAILED);
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return CommonResult.success(Integer.toString(code));

    }

    public SendSmsResponse sendSms(String telephone, String code) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "60000");
        System.setProperty("sun.net.client.defaultReadTimeout", "60000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(telephone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_93735028");
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            System.out.println("短信发送成功！");
        } else {
            System.out.println("短信发送失败！");
        }
        return sendSmsResponse;
    }

    //以下为测试代码，随机生成验证码
    private static int newcode;

    public static int getNewcode() {
        return newcode;
    }

    public static void setNewcode() {
        newcode = (int) (Math.random() * 9999) + 100;  //每次调用生成一位四位数的随机数
    }


}
