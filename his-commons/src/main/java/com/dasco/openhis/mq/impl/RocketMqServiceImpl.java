package com.dasco.openhis.mq.impl;

import com.alibaba.fastjson.JSON;
import com.dasco.openhis.mq.dto.BaseMqDto;
import com.dasco.openhis.mq.service.RocketMqService;
import com.dasco.openhis.utils.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RocketMqServiceImpl implements RocketMqService {

    @Lazy
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void sendDelayed(String topic, BaseMqDto<?> data, int delayLevel) {
        ValidateUtils.notNullParam(topic);
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getData());
        ValidateUtils.isTrue(delayLevel >= 0, "延迟级别参数必须大于等于0");
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(data).build(), new SendCallback() {
            public void onSuccess(SendResult res) {
                log.info("sendDelayed - onSuccess - topic：{} data:{} sendResult:{}",topic, JSON.toJSONString(data),JSON.toJSONString(res));
            }
            public void onException(Throwable e) {
                log.error("sendDelayed - onException - topic：{} data:{} e:",topic,JSON.toJSONString(data),e);
            }
        },5000,delayLevel);
    }
}
