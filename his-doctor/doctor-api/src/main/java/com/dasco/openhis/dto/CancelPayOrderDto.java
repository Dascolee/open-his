package com.dasco.openhis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelPayOrderDto implements Serializable {

    private static final long serialVersionUID = 1l;

    private String orderId;
}
