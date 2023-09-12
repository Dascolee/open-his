package com.dasco.openhis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkloadStat extends BaseEntity{

    private String userId;  //医生id

    private String doctorName;      //医生姓名

    private BigDecimal totalAmount;     //挂号总金额

    private Integer count;      //挂号次数
}
