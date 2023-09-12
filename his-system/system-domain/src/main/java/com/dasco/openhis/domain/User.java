package com.dasco.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @TableName sys_user
 */
@TableName(value ="sys_user")
@Data
public class User extends BaseEntity implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 部门ID
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 用户账号
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户类型（0超级用户为 1为系统用户）
     */
    @TableField(value = "user_type")
    private String userType;

    /**
     * 用户性别（0男 1女 2未知）
     */
    @TableField(value = "sex")
    private String sex;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 头像
     */
    @TableField(value = "picture")
    private String picture;

    /**
     * 学历 sys_dict_type:sys_user_background
     */
    @TableField(value = "background")
    private String background;

    /**
     * 电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 擅长
     */
    @TableField(value = "strong")
    private String strong;

    /**
     * 荣誉
     */
    @TableField(value = "honor")
    private String honor;

    /**
     * 简介
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 最后一次登录时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    /**
     * 最后登陆IP
     */
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    /**
     * 帐号状态（0正常 1停用）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 
     */
    @TableField(value = "union_id")
    private String unionId;

    /**
     * 用户授权登录openid 扩展第三方登陆使用
     */
    @TableField(value = "open_id")
    private String openId;

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

    /**
     * 盐
     */
    @TableField(value = "salt")
    private String salt;

    /**
     * 删除标志（0正常 1删除）
     */
    @TableField(value = "del_flag")
    private String delFlag;

    /**
     * 是否需要参与排班0需要,1 不需要
     */
    @TableField(value = "scheduling_flag")
    private String schedulingFlag;

    /**
     * 
医生级别sys_dict_type:sys_user_level
     */
    @TableField(value = "user_rank")
    private String userRank;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static final String COL_USER_ID = "user_id";

    public static final String COL_DEPT_ID = "dept_id";

    public static final String COL_USER_NAME = "user_name";

    public static final String COL_USER_TYPE = "user_type";

    public static final String COL_SEX = "sex";

    public static final String COL_AGE = "age";

    public static final String COL_PICTURE = "picture";

    public static final String COL_BACKGROUND = "background";

    public static final String COL_PHONE = "phone";

    public static final String COL_EMAIL = "email";

    public static final String COL_STRONG = "strong";

    public static final String COL_HONOR = "honor";

    public static final String COL_INTRODUCTION = "introduction";

    public static final String COL_USER_RANK = "user_rank";

    public static final String COL_PASSWORD = "password";

    public static final String COL_LAST_LOGIN_TIME = "last_login_time";

    public static final String COL_LAST_LOGIN_IP = "last_login_ip";

    public static final String COL_STATUS = "status";

    public static final String COL_UNION_ID = "union_id";

    public static final String COL_OPEN_ID = "open_id";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_SALT = "salt";

    public static final String COL_SCHEDULING_FLAG = "scheduling_flag";
}