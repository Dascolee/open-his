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
public class CheckStat extends BaseEntity {

    private Long checkItemId;       //检查项目id

    private String checkItemName;       //检查项目名称

    private BigDecimal totalAmount;     //总金额

    private Integer count;  //检查次数
}
