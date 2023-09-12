package com.dasco.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 药品信息表
 * @TableName stock_medicines
 */
@TableName(value ="stock_medicines")
@Data
public class Medicines extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "medicines_id", type = IdType.AUTO)
    private Long medicinesId;

    /**
     * 药品编号
     */
    @TableField(value = "medicines_number")
    private String medicinesNumber;

    /**
     * 药品名称
     */
    @TableField(value = "medicines_name")
    private String medicinesName;

    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @TableField(value = "medicines_type")
    private String medicinesType;

    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @TableField(value = "prescription_type")
    private String prescriptionType;

    /**
     * 处方价格
     */
    @TableField(value = "prescription_price")
    private BigDecimal prescriptionPrice;

    /**
     * 单位（g/条）
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 换算量
     */
    @TableField(value = "conversion")
    private Integer conversion;

    /**
     * 关键字
     */
    @TableField(value = "keywords")
    private String keywords;

    /**
     * 生产厂家ID
     */
    @TableField(value = "producter_id")
    private String producterId;

    /**
     * 药品状态0正常0停用 sys_dict_data表 sys_normal_disable
     */
    @TableField(value = "status")
    private String status;

    /**
     * 库存量
     */
    @TableField(value = "medicines_stock_num")
    private Integer medicinesStockNum;

    /**
     * 预警值
     */
    @TableField(value = "medicines_stock_danger_num")
    private Integer medicinesStockDangerNum;

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
     * 删除状态0正常0删除 要有重新导入功能
     */
    @TableField(value = "del_flag")
    private String delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static final String COL_MEDICINES_ID = "medicines_id";

    public static final String COL_MEDICINES_NUMBER = "medicines_number";

    public static final String COL_MEDICINES_NAME = "medicines_name";

    public static final String COL_MEDICINES_TYPE = "medicines_type";

    public static final String COL_PRESCRIPTION_TYPE = "prescription_type";

    public static final String COL_PRESCRIPTION_PRICE = "prescription_price";

    public static final String COL_UNIT = "unit";

    public static final String COL_CONVERSION = "conversion";

    public static final String COL_KEYWORDS = "keywords";

    public static final String COL_PRODUCTER_ID = "producter_id";

    public static final String COL_STATUS = "status";

    public static final String COL_MEDICINES_STOCK_NUM = "medicines_stock_num";

    public static final String COL_MEDICINES_STOCK_DANGER_NUM = "medicines_stock_danger_num";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}