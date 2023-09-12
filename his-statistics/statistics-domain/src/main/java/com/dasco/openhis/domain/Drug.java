package com.dasco.openhis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Drug extends BaseEntity{

    //药品id
    private String medicinesId;

    //药品名称
    private String medicinesName;

    //药品价格
    private BigDecimal price;

    //销售数量
    private BigDecimal num;

    //销售金额
    private BigDecimal amount;

    //创建时间
    private Date createTime;
}
