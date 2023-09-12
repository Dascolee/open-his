package com.dasco.openhis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CheckQueryDto extends BaseDto{

    private String checkItemId;     //检查项目id

    private String patientName;     //病人名称

    private String queryDate;       //查询日期
}
