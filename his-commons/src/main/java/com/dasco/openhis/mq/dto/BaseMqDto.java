package com.dasco.openhis.mq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseMqDto<T> implements Serializable {

    private static final long serialVersionUID = -1l;

    private T data;     //消息体

    private String messageId;
}
