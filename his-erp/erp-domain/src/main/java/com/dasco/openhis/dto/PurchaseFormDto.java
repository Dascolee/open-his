package com.dasco.openhis.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:
 */

@ApiModel(value="com-dasco-openhis-dto-PurchaseFromDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseFormDto extends BaseDto {

    //存放采购单主表数据
    private PurchaseDto purchaseDto;

    //存放采购单详情数据
    private List<PurchaseItemDto> purchaseItemDtos;
}
