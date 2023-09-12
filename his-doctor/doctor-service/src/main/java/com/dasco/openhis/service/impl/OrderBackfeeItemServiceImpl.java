package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.OrderBackfeeItem;
import com.dasco.openhis.mapper.OrderBackfeeItemMapper;
import com.dasco.openhis.service.OrderBackfeeItemService;
import org.springframework.stereotype.Service;

/**
* @author a
* @description 针对表【his_order_backfee_item(退费订单详情表)】的数据库操作Service实现
*/
@Service
public class OrderBackfeeItemServiceImpl extends ServiceImpl<OrderBackfeeItemMapper, OrderBackfeeItem>
    implements OrderBackfeeItemService{

}




