package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.OrderBackfee;
import com.dasco.openhis.domain.OrderBackfeeItem;
import com.dasco.openhis.dto.OrderBackfeeDto;
import com.dasco.openhis.dto.OrderBackfeeFormDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【his_order_backfee(退费主表)】的数据库操作Service
*/
public interface OrderBackfeeService extends IService<OrderBackfee> {

    void saveOrderAndItems(OrderBackfeeFormDto orderBackfeeFormDto);

    void backSuccess(String backId, String backPlatformId, String payType0);

    DataGridView queryAllOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto);

    List<OrderBackfeeItem> queryOrderBackfeeItemByBackId(String backId);
}
