package com.dasco.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName his_verification_code
 */
@TableName(value ="his_verification_code")
@Data
public class VerificationCode extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 验证码
     */
    @TableField(value = "verification_code")
    private Integer verificationCode;

    /**
     * 手机号
     */
    @TableField(value = "phone_number")
    private String phoneNumber;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 是否已校验
     */
    @TableField(value = "is_check")
    private Integer isCheck;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static final String VERIFICATION_CODE = "verification_code";

    public static final String PHONE_NUMBER = "phone_number";

    public static final String IS_CHECK = "is_check";
}