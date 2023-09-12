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
public class Check extends BaseEntity{

    private Long checkItemId;

    private String checkItemName;

    private BigDecimal price;

    private String patientId;

    private String patientName;

    private String resultStatus;    //0-检查中 1-检查完成

    private Date createTime;

    private String createBy;
}
