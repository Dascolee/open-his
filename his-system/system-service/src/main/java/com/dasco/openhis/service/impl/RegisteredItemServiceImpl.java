package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.RegisteredItem;
import com.dasco.openhis.dto.RegisteredItemDto;
import com.dasco.openhis.mapper.RegisteredItemMapper;
import com.dasco.openhis.service.RegisteredItemService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* @author a
* @description 针对表【sys_registered_item】的数据库操作Service实现
*/
@Service
public class RegisteredItemServiceImpl extends ServiceImpl<RegisteredItemMapper, RegisteredItem> implements RegisteredItemService {

    @Autowired
    private RegisteredItemMapper registeredItemMapper;

    @Override
    public DataGridView listRegisteredItemForPage(RegisteredItemDto registeredItemDto) {
        Page<RegisteredItem> page = new Page<>(registeredItemDto.getPageNum(), registeredItemDto.getPageSize());
        QueryWrapper<RegisteredItem> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(registeredItemDto.getRegItemName()),RegisteredItem.COL_REG_ITEM_NAME,
                registeredItemDto.getRegItemName());
        wrapper.eq(StringUtils.isNotBlank(registeredItemDto.getStatus()),RegisteredItem.COL_STATUS,
                registeredItemDto.getStatus());
        registeredItemMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem registeredItem = new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,registeredItem);
        registeredItem.setCreateBy(registeredItemDto.getSimpleUser().getUserName());
        registeredItem.setCreateTime(DateUtil.date());
        return registeredItemMapper.insert(registeredItem);
    }

    @Override
    public int updateRegisteredItem(RegisteredItemDto registeredItemDto) {
        RegisteredItem registeredItem = new RegisteredItem();
        BeanUtil.copyProperties(registeredItemDto,registeredItem);
        registeredItem.setUpdateBy(registeredItemDto.getSimpleUser().getUserName());
        return registeredItemMapper.updateById(registeredItem);
    }

    @Override
    public RegisteredItem getOne(Long registeredItemId) {
        return registeredItemMapper.selectById(registeredItemId);
    }

    @Override
    public int deleteRegisteredItemByIds(Long[] registeredItemIds) {
        List<Long> ids = Arrays.asList(registeredItemIds);
        if(ids != null && ids.size() > 0){
            return registeredItemMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<RegisteredItem> selectAllRegisteredItem() {
        QueryWrapper<RegisteredItem> qw=new QueryWrapper<>();
        qw.eq(RegisteredItem.COL_STATUS, Constants.STATUS_TRUE);
        return this.registeredItemMapper.selectList(qw);
    }
}




