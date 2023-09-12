package com.dasco.openhis.service.listener;

import com.alibaba.fastjson.JSON;
import com.dasco.openhis.constants.MQConstants;
import com.dasco.openhis.dto.CancelPayOrderDto;
import com.dasco.openhis.mq.dto.BaseMqDto;
import com.dasco.openhis.service.OrderChargeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstants.Topic.CANCEL_PAY_ORDER,consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstants.Topic.CANCEL_PAY_ORDER)
public class CancelPayOrderListener implements RocketMQListener<BaseMqDto<CancelPayOrderDto>> {

    @Autowired
    private OrderChargeService orderChargeService;

    @Override
    public void onMessage(BaseMqDto<CancelPayOrderDto> data) {
        log.info("onMessage - data:{}", JSON.toJSONString(data));
        CancelPayOrderDto cancelPayOrderDto = data.getData();
        //更新订单状态为已取消
        orderChargeService.cancelPayOrder(cancelPayOrderDto.getOrderId());
    }
}
