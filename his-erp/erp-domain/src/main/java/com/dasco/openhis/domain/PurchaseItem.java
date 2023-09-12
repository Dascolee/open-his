package com.dasco.openhis.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName stock_purchase_item
 */
@TableName(value ="stock_purchase_item")
@Data
public class PurchaseItem extends BaseEntity implements Serializable {
    /**
     * 详情ID
     */
    @TableId(value = "item_id", type = IdType.INPUT)
    private String itemId;

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
     * 采购数量
     */
    @TableField(value = "purchase_number")
    private Integer purchaseNumber;

    /**
     * 批发价
     */
    @TableField(value = "trade_price")
    private BigDecimal tradePrice;

    /**
     * 批发额
     */
    @TableField(value = "trade_total_amount")
    private BigDecimal tradeTotalAmount;

    /**
     * 药品生产批次号
     */
    @TableField(value = "batch_number")
    private String batchNumber;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
     * 关键字
     */
    @TableField(value = "keywords")
    private String keywords;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public static final String COL_ITEM_ID = "item_id";

    public static final String COL_PURCHASE_ID = "purchase_id";

    public static final String COL_PURCHASE_NUMBER = "purchase_number";

    public static final String COL_TRADE_PRICE = "trade_price";

    public static final String COL_TRADE_TOTAL_AMOUNT = "trade_total_amount";

    public static final String COL_BATCH_NUMBER = "batch_number";

    public static final String COL_REMARK = "remark";

    public static final String COL_MEDICINES_ID = "medicines_id";

    public static final String COL_MEDICINES_NAME = "medicines_name";

    public static final String COL_MEDICINES_TYPE = "medicines_type";

    public static final String COL_PRESCRIPTION_TYPE = "prescription_type";

    public static final String COL_PRODUCTER_ID = "producter_id";

    public static final String COL_CONVERSION = "conversion";

    public static final String COL_UNIT = "unit";

    public static final String COL_KEYWORDS = "keywords";
}