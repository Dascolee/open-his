package com.dasco.openhis.mq.service;

import com.dasco.openhis.mq.dto.BaseMqDto;

public interface RocketMqService {

    void sendDelayed(String topic, BaseMqDto<?> data, int delayLevel);
}
