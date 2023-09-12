package com.dasco.openhis.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
* @Author:
*/

@ApiModel(value="com-dasco-dto-RegisteredItemDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredItemDto extends BaseDto {
    /**
     * 挂号项ID
     */
    @ApiModelProperty(value="挂号项ID")
    private Long regItemId;

    /**
     * 挂号项目名称
     */
    @NotBlank(message = "挂号项目名称不能为空")
    @ApiModelProperty(value="挂号项目名称")
    private String regItemName;

    /**
     * 金额
     */
    @NotNull(message = "挂号项目金额不能为空")
    @ApiModelProperty(value="金额")
    private BigDecimal regItemFee;

    /**
     * 状态（0正常 1停用）
     */
    @NotBlank(message = "挂号项目状态不能为空")
    @ApiModelProperty(value="状态（0正常 1停用）")
    private String status;

}