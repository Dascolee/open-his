package com.dasco.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商信息表
 * @TableName stock_provider
 */
@TableName(value ="stock_provider")
@Data
public class Provider extends BaseEntity implements Serializable {
    /**
     * 供应商id
     */
    @TableId(value = "provider_id", type = IdType.AUTO)
    private Long providerId;

    /**
     * 供应商名称
     */
    @TableField(value = "provider_name")
    private String providerName;

    /**
     * 联系人名称
     */
    @TableField(value = "contact_name")
    private String contactName;

    /**
     * 联系人手机
     */
    @TableField(value = "contact_mobile")
    private String contactMobile;

    /**
     * 联系人电话
     */
    @TableField(value = "contact_tel")
    private String contactTel;

    /**
     * 银行账号
     */
    @TableField(value = "bank_account")
    private String bankAccount;

    /**
     * 供应商地址
     */
    @TableField(value = "provider_address")
    private String providerAddress;

    /**
     * 状态（0正常 1停用）sys_normal_disable
     */
    @TableField(value = "status")
    private String status;

    /**
     * 删除标志（0正常 1删除）
     */
    @TableField(value = "del_flag")
    private String delFlag;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static final String COL_PROVIDER_ID = "provider_id";

    public static final String COL_PROVIDER_NAME = "provider_name";

    public static final String COL_CONTACT_NAME = "contact_name";

    public static final String COL_CONTACT_MOBILE = "contact_mobile";

    public static final String COL_CONTACT_TEL = "contact_tel";

    public static final String COL_BANK_ACCOUNT = "bank_account";

    public static final String COL_PROVIDER_ADDRESS = "provider_address";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}