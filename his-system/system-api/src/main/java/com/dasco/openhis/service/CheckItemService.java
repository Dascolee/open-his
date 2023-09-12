package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.CheckItem;
import com.dasco.openhis.dto.CheckItemDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【sys_check_item(检查费用表)】的数据库操作Service
*/
public interface CheckItemService extends IService<CheckItem> {

    DataGridView listCheckItemForPage(CheckItemDto checkItemDto);

    int addCheckItem(CheckItemDto checkItemDto);

    int updateCheckItem(CheckItemDto checkItemDto);

    CheckItem getOne(Long checkItemId);

    int deleteCheckItemByIds(Long[] checkItemIds);

    List<CheckItem> selectAllCheckItem();
}
