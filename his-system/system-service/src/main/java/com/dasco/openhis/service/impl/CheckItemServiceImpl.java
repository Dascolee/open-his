package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.CheckItem;
import com.dasco.openhis.dto.CheckItemDto;
import com.dasco.openhis.mapper.CheckItemMapper;
import com.dasco.openhis.service.CheckItemService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* @author a
* @description 针对表【sys_check_item(检查费用表)】的数据库操作Service实现
*/
@Service
public class CheckItemServiceImpl extends ServiceImpl<CheckItemMapper, CheckItem> implements CheckItemService {

    @Resource
    private CheckItemMapper checkItemMapper;

    @Override
    public DataGridView listCheckItemForPage(CheckItemDto checkItemDto) {
        Page<CheckItem> page = new Page<>(checkItemDto.getPageNum(), checkItemDto.getPageSize());
        QueryWrapper<CheckItem> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(checkItemDto.getCheckItemName()),CheckItem.COL_CHECK_ITEM_NAME,
                checkItemDto.getCheckItemName());
        wrapper.like(StringUtils.isNotBlank(checkItemDto.getKeywords()),CheckItem.COL_KEYWORDS,
                checkItemDto.getKeywords());
        wrapper.eq(StringUtils.isNotBlank(checkItemDto.getTypeId()),CheckItem.COL_TYPE_ID,
                checkItemDto.getTypeId());
        wrapper.eq(StringUtils.isNotBlank(checkItemDto.getStatus()),CheckItem.COL_STATUS,
                checkItemDto.getStatus());
        checkItemMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addCheckItem(CheckItemDto checkItemDto) {
        CheckItem checkItem = new CheckItem();
        BeanUtil.copyProperties(checkItemDto,checkItem);
        checkItem.setCreateBy(checkItemDto.getSimpleUser().getUserName());
        checkItem.setCreateTime(DateUtil.date());
        return checkItemMapper.insert(checkItem);
    }

    @Override
    public int updateCheckItem(CheckItemDto checkItemDto) {
        CheckItem checkItem = new CheckItem();
        BeanUtil.copyProperties(checkItemDto,checkItem);
        checkItem.setUpdateBy(checkItemDto.getSimpleUser().getUserName());
        return checkItemMapper.updateById(checkItem);
    }

    @Override
    public CheckItem getOne(Long checkItemId) {
        return checkItemMapper.selectById(checkItemId);
    }

    @Override
    public int deleteCheckItemByIds(Long[] checkItemIds) {
        List<Long> ids = Arrays.asList(checkItemIds);
        if(ids != null && ids.size() > 0){
            return checkItemMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<CheckItem> selectAllCheckItem() {
        QueryWrapper<CheckItem> wrapper = new QueryWrapper<>();
        wrapper.eq(CheckItem.COL_STATUS,Constants.STATUS_TRUE);
        return checkItemMapper.selectList(wrapper);
    }


}




