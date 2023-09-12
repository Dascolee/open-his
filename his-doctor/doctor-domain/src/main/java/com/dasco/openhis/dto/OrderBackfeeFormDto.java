package com.dasco.openhis.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author:
 */
@ApiModel(value="com-dasco-dto-OrderBackfeeFormDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderBackfeeFormDto extends BaseDto {

    //主订单
    private com.dasco.openhis.dto.OrderBackfeeDto orderBackfeeDto;

    //订单详情
    @NotEmpty(message = "退费订单详情不能为空")
    private List<com.dasco.openhis.dto.OrderBackfeeItemDto> orderBackfeeItemDtoList;

}
