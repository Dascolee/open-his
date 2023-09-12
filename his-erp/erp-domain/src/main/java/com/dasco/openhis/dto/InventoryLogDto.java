package com.dasco.openhis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author:
 */

@ApiModel(value = "com-dasco-openhis-dto-InventoryLogDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLogDto extends BaseDto {

    /**
     * 采购单据ID
     */
    @ApiModelProperty(value = "采购单据ID")
    private String purchaseId;

    /**
     * 药品名称
     */
    @ApiModelProperty(value = "药品名称")
    private String medicinesName;

    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @ApiModelProperty(value = "药品分类 sys_dict_data表his_medicines_type")
    private String medicinesType;

    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @ApiModelProperty(value = "处方类型 sys_dict_data表his_prescription_type")
    private String prescriptionType;

    /**
     * 生产厂家ID
     */
    @ApiModelProperty(value = "生产厂家ID")
    private String producterId;
}