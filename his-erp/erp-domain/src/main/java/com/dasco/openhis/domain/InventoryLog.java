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
 * 
 * @TableName stock_inventory_log
 */
@TableName(value ="stock_inventory_log")
@Data
public class InventoryLog extends BaseEntity implements Serializable {
    /**
     * 入库ID
     */
    @TableId(value = "inventory_log_id", type = IdType.INPUT)
    private String inventoryLogId;

    /**
     * 采购单据ID
     */
    @TableField(value = "purchase_id")
    private String purchaseId;

    /**
     * 药品ID
     */
    @TableField(value = "medicines_id")
    private String medicinesId;

    /**
     * 入库数量
     */
    @TableField(value = "inventory_log_num")
    private Integer inventoryLogNum;

    /**
     * 批发价
     */
    @TableField(value = "trade_price")
    private BigDecimal tradePrice;

    /**
     * 处方价
     */
    @TableField(value = "prescription_price")
    private BigDecimal prescriptionPrice;

    /**
     * 批发额
     */
    @TableField(value = "trade_total_amount")
    private BigDecimal tradeTotalAmount;

    /**
     * 处方额
     */
    @TableField(value = "prescription_total_amount")
    private BigDecimal prescriptionTotalAmount;

    /**
     * 药品生产批次号
     */
    @TableField(value = "batch_number")
    private String batchNumber;

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
     * 生产厂家ID
     */
    @TableField(value = "producter_id")
    private String producterId;

    /**
     * 换算量
     */
    @TableField(value = "conversion")
    private Integer conversion;

    /**
     * 单位（g/条）
     */
    @TableField(value = "unit")
    private String unit;

    /**
     * 供应商ID
     */
    @TableField(value = "provider_id")
    private String providerId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    private String createBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static final String COL_INVENTORY_LOG_ID = "inventory_log_id";

    public static final String COL_PURCHASE_ID = "purchase_id";

    public static final String COL_MEDICINES_ID = "medicines_id";

    public static final String COL_INVENTORY_LOG_NUM = "inventory_log_num";

    public static final String COL_TRADE_PRICE = "trade_price";

    public static final String COL_TRADE_TOTAL_AMOUNT = "trade_total_amount";

    public static final String COL_BATCH_NUMBER = "batch_number";

    public static final String COL_MEDICINES_NAME = "medicines_name";

    public static final String COL_MEDICINES_TYPE = "medicines_type";

    public static final String COL_PRESCRIPTION_TYPE = "prescription_type";

    public static final String COL_PRODUCTER_ID = "producter_id";

    public static final String COL_CONVERSION = "conversion";

    public static final String COL_UNIT = "unit";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CREATE_BY = "create_by";
}