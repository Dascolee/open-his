package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.PurchaseItem;
import com.dasco.openhis.mapper.PurchaseItemMapper;
import com.dasco.openhis.service.PurchaseItemService;
import org.springframework.stereotype.Service;

/**
* @author a
* @description 针对表【stock_purchase_item】的数据库操作Service实现
*/
@Service
public class PurchaseItemServiceImpl extends ServiceImpl<PurchaseItemMapper, PurchaseItem>
    implements PurchaseItemService {

}




