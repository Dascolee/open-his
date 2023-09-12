package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.OrderCharge;
import com.dasco.openhis.domain.OrderChargeItem;
import com.dasco.openhis.dto.OrderChargeDto;
import com.dasco.openhis.dto.OrderChargeFromDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【his_order_charge(收费表)】的数据库操作Service
*/
public interface OrderChargeService extends IService<OrderCharge> {

    void saveOrderAndItems(OrderChargeFromDto orderChargeFromDto);

    void paySuccess(String orderId, String platFormId, String payType0);

    OrderCharge queryOrderChargeByOrderId(String orderId);

    DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto);

    List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId);

    OrderChargeItem queryOrderChargeItemByItemId(String itemId);

    void cancelPayOrder(String orderId);
}
