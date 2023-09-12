package com.dasco.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 生产厂家表
 * @TableName stock_producer
 */
@TableName(value ="stock_producer")
@Data
public class Producer extends BaseEntity implements Serializable {
    /**
     * 厂家ID
     */
    @TableId(value = "producer_id", type = IdType.AUTO)
    private Long producerId;

    /**
     * 厂家名称
     */
    @TableField(value = "producer_name")
    private String producerName;

    /**
     * 厂家简码 搜索用
     */
    @TableField(value = "producer_code")
    private String producerCode;

    /**
     * 厂家地址 
     */
    @TableField(value = "producer_address")
    private String producerAddress;

    /**
     * 厂家电话
     */
    @TableField(value = "producer_tel")
    private String producerTel;

    /**
     * 联系人
     */
    @TableField(value = "producer_person")
    private String producerPerson;

    /**
     * 关键字
     */
    @TableField(value = "keywords")
    private String keywords;

    /**
     * 状态标志（0正常 1停用）sys_normal_disable
     */
    @TableField(value = "status")
    private String status;

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

    public static final String COL_PRODUCTER_ID = "producer_id";

    public static final String COL_PRODUCTER_NAME = "producer_name";

    public static final String COL_PRODUCTER_CODE = "producer_code";

    public static final String COL_PRODUCTER_ADDRESS = "producer_address";

    public static final String COL_PRODUCTER_TEL = "producer_tel";

    public static final String COL_PRODUCTER_PERSON = "producer_person";

    public static final String COL_KEYWORDS = "keywords";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}