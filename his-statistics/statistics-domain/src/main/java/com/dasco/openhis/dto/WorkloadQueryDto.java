package com.dasco.openhis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WorkloadQueryDto extends BaseDto{

    private String doctorName;

    private String queryDate;
}
