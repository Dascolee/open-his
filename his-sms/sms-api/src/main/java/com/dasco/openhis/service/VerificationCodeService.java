package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.VerificationCode;

/**
* @author li118
* @description 针对表【his_verification_code】的数据库操作Service
* @createDate 2023-07-21 12:55:11
*/
public interface VerificationCodeService extends IService<VerificationCode> {
    int sendSms(String phoneNumber);
    VerificationCode checkCode(String phoneNumber, Integer code);
}
