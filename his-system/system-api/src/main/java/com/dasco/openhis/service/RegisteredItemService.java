package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.RegisteredItem;
import com.dasco.openhis.dto.RegisteredItemDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【sys_registered_item】的数据库操作Service
*/
public interface RegisteredItemService extends IService<RegisteredItem> {

    DataGridView listRegisteredItemForPage(RegisteredItemDto registeredItemDto);

    int addRegisteredItem(RegisteredItemDto registeredItemDto);

    int updateRegisteredItem(RegisteredItemDto registeredItemDto);

    RegisteredItem getOne(Long registeredItemId);

    int deleteRegisteredItemByIds(Long[] registeredItemIds);

    List<RegisteredItem> selectAllRegisteredItem();
}
