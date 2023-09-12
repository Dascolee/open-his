package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.InventoryLog;
import com.dasco.openhis.dto.InventoryLogDto;
import com.dasco.openhis.vo.DataGridView;

/**
* @author a
* @description 针对表【stock_inventory_log】的数据库操作Service
*/
public interface InventoryLogService extends IService<InventoryLog> {

    DataGridView listInventoryLogForPage(InventoryLogDto inventoryLogDto);
}
