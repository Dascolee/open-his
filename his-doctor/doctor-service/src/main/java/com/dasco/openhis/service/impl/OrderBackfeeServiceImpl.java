package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.*;
import com.dasco.openhis.dto.*;
import com.dasco.openhis.mapper.CareOrderItemMapper;
import com.dasco.openhis.mapper.OrderBackfeeItemMapper;
import com.dasco.openhis.mapper.OrderBackfeeMapper;
import com.dasco.openhis.mapper.OrderChargeItemMapper;
import com.dasco.openhis.service.OrderBackfeeService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
* @author a
* @description 针对表【his_order_backfee(退费主表)】的数据库操作Service实现
*/
@Service
public class OrderBackfeeServiceImpl extends ServiceImpl<OrderBackfeeMapper, OrderBackfee> implements OrderBackfeeService{

    @Autowired
    private OrderBackfeeMapper orderBackfeeMapper;

    @Autowired
    private OrderBackfeeItemMapper orderBackfeeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Override
    public void saveOrderAndItems(OrderBackfeeFormDto orderBackfeeFormDto) {
        OrderBackfeeDto orderBackfeeDto = orderBackfeeFormDto.getOrderBackfeeDto();
        List<OrderBackfeeItemDto> backfeeItemDtoList = orderBackfeeFormDto.getOrderBackfeeItemDtoList();
        //保存主表
        OrderBackfee orderBackfee = new OrderBackfee();
        BeanUtil.copyProperties(orderBackfeeDto,orderBackfee);
        orderBackfee.setBackStatus(Constants.ORDER_STATUS_0);
        orderBackfee.setCreateBy(orderBackfeeFormDto.getSimpleUser().getUserName());
        orderBackfee.setCreateTime(DateUtil.date());
        orderBackfeeMapper.insert(orderBackfee);
        //保存详情
        for (OrderBackfeeItemDto orderBackfeeItemDto : backfeeItemDtoList) {
            OrderBackfeeItem orderBackfeeItem = new OrderBackfeeItem();
            BeanUtil.copyProperties(orderBackfeeItemDto,orderBackfeeItem);
            orderBackfeeItem.setBackId(orderBackfee.getBackId());
            orderBackfeeItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
            orderBackfeeItemMapper.insert(orderBackfeeItem);
        }
    }

    @Override
    public void backSuccess(String backId, String backPlatformId, String payType0) {
        //1根据backID查询退费主表
        OrderBackfee orderBackfee = orderBackfeeMapper.selectById(backId);
        orderBackfee.setBackPlatformId(backPlatformId);
        orderBackfee.setBackType(payType0);
        orderBackfee.setBackTime(DateUtil.date());
        orderBackfee.setBackStatus(Constants.ORDER_BACKFEE_STATUS_1);
        //2更新主表状态
        orderBackfeeMapper.updateById(orderBackfee);
        //3根据主表id查询退费详情表
        QueryWrapper<OrderBackfeeItem> wrapper = new QueryWrapper<>();
        wrapper.eq(OrderBackfeeItem.COL_BACK_ID,backId);
        List<OrderBackfeeItem> backfeeItems = orderBackfeeItemMapper.selectList(wrapper);
        ArrayList<String> allItemIds = new ArrayList<>();
        for (OrderBackfeeItem orderBackfeeItem : backfeeItems) {
            allItemIds.add(orderBackfeeItem.getItemId());
        }
        //4更新退费详情表状态
        OrderBackfeeItem orderBackfeeItem = new OrderBackfeeItem();
        orderBackfeeItem.setStatus(Constants.ORDER_DETAILS_STATUS_2);
        QueryWrapper<OrderBackfeeItem> wrapper1 = new QueryWrapper<>();
        wrapper1.in(OrderBackfeeItem.COL_ITEM_ID,allItemIds);
        orderBackfeeItemMapper.update(orderBackfeeItem,wrapper1);
        //5更新收费明细表状态
        OrderChargeItem orderChargeItem = new OrderChargeItem();
        orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_2);
        QueryWrapper<OrderChargeItem> wrapper3 = new QueryWrapper<>();
        wrapper3.in(OrderChargeItem.COL_ITEM_ID,allItemIds);
        orderChargeItemMapper.update(orderChargeItem,wrapper3);
        //6更新处方明细表状态
        CareOrderItem careOrderItem = new CareOrderItem();
        careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_2);
        QueryWrapper<CareOrderItem> wrapper2 = new QueryWrapper<>();
        wrapper2.in(CareOrderItem.COL_ITEM_ID,allItemIds);
        careOrderItemMapper.update(careOrderItem,wrapper2);
    }

    @Override
    public DataGridView queryAllOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto) {
        Page<OrderBackfee> page = new Page<>(orderBackfeeDto.getPageNum(), orderBackfeeDto.getPageSize());
        QueryWrapper<OrderBackfee> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(orderBackfeeDto.getPatientName()),OrderBackfee.COL_PATIENT_NAME,
                orderBackfeeDto.getPatientName());
        wrapper.like(StringUtils.isNotBlank(orderBackfeeDto.getRegId()),OrderBackfee.COL_REG_ID,
                orderBackfeeDto.getRegId());
        orderBackfeeMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public List<OrderBackfeeItem> queryOrderBackfeeItemByBackId(String backId) {
        QueryWrapper<OrderBackfeeItem> wrapper = new QueryWrapper<>();
        wrapper.eq(OrderBackfeeItem.COL_BACK_ID,backId);
        return orderBackfeeItemMapper.selectList(wrapper);
    }
}