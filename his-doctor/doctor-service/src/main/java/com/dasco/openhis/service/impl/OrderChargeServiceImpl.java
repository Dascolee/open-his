package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.constants.MQConstants;
import com.dasco.openhis.domain.CareOrderItem;
import com.dasco.openhis.domain.OrderCharge;
import com.dasco.openhis.domain.OrderChargeItem;
import com.dasco.openhis.dto.CancelPayOrderDto;
import com.dasco.openhis.dto.OrderChargeDto;
import com.dasco.openhis.dto.OrderChargeFromDto;
import com.dasco.openhis.dto.OrderChargeItemDto;
import com.dasco.openhis.mapper.CareOrderItemMapper;
import com.dasco.openhis.mapper.OrderChargeItemMapper;
import com.dasco.openhis.mapper.OrderChargeMapper;
import com.dasco.openhis.mq.dto.BaseMqDto;
import com.dasco.openhis.mq.service.RocketMqService;
import com.dasco.openhis.service.OrderChargeService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author a
 * @description 针对表【his_order_charge(收费表)】的数据库操作Service实现
 */
@Service
public class OrderChargeServiceImpl extends ServiceImpl<OrderChargeMapper, OrderCharge> implements OrderChargeService{

    @Autowired
    private OrderChargeMapper orderChargeMapper;

    @Autowired
    private OrderChargeItemMapper orderChargeItemMapper;

    @Autowired
    private CareOrderItemMapper careOrderItemMapper;

    @Autowired
    private RocketMqService rocketMqService;

    /**
     * 保存订单及详情
     * @param orderChargeFromDto
     */
    @Override
    public void saveOrderAndItems(OrderChargeFromDto orderChargeFromDto) {
        OrderChargeDto orderChargeDto = orderChargeFromDto.getOrderChargeDto();
        List<OrderChargeItemDto> orderChargeItemDtoList = orderChargeFromDto.getOrderChargeItemDtoList();
        //保存主表
        OrderCharge orderCharge = new OrderCharge();
        BeanUtil.copyProperties(orderChargeDto,orderCharge);
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_0);
        orderCharge.setCreateBy(orderChargeFromDto.getSimpleUser().getUserName());
        orderCharge.setCreateTime(DateUtil.date());
        orderChargeMapper.insert(orderCharge);
        //保存详情
        for (OrderChargeItemDto orderChargeItemDto : orderChargeItemDtoList) {
            OrderChargeItem orderChargeItem = new OrderChargeItem();
            BeanUtil.copyProperties(orderChargeItemDto,orderChargeItem);
            orderChargeItem.setOrderId(orderCharge.getOrderId());
            orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_0);
            orderChargeItemMapper.insert(orderChargeItem);
        }
        if(Constants.PAY_TYPE_0.equals(orderChargeDto.getPayType())){
            //do nothing
        }else{
            rocketMqService.sendDelayed(MQConstants.Topic.CANCEL_PAY_ORDER,
                    new BaseMqDto<>(new CancelPayOrderDto(orderCharge.getOrderId()), UUID.randomUUID().toString()),
                    MQConstants.DelayLevel.level_6);
        }
    }

    /**
     * 支付成功的回调
     * @param orderId   订单主表id
     * @param platFormId  平台id（现金为空）
     * @param payType0  支付方式 0-现金 1-支付宝
     */
    @Override
    public void paySuccess(String orderId, String platFormId, String payType0) {
        //1根据orderID查询订单主表
        OrderCharge orderCharge = orderChargeMapper.selectById(orderId);
        orderCharge.setPayPlatformId(platFormId);
        orderCharge.setPayType(payType0);
        orderCharge.setPayTime(DateUtil.date());
        orderCharge.setOrderStatus(Constants.ORDER_STATUS_1);
        //2更新主表状态
        orderChargeMapper.updateById(orderCharge);
        //3根据主表id查询订单详情表
        QueryWrapper<OrderChargeItem> wrapper = new QueryWrapper<>();
        wrapper.eq(OrderChargeItem.COL_ORDER_ID,orderId);
        List<OrderChargeItem> orderChargeItems = orderChargeItemMapper.selectList(wrapper);
        ArrayList<String> allItemIds = new ArrayList<>();
        for (OrderChargeItem orderChargeItem : orderChargeItems) {
            allItemIds.add(orderChargeItem.getItemId());
        }
        //4更新详情表状态
        OrderChargeItem orderChargeItem = new OrderChargeItem();
        orderChargeItem.setStatus(Constants.ORDER_DETAILS_STATUS_1);
        QueryWrapper<OrderChargeItem> wrapper1 = new QueryWrapper<>();
        wrapper1.in(OrderChargeItem.COL_ITEM_ID,allItemIds);
        orderChargeItemMapper.update(orderChargeItem,wrapper1);
        //5更新处方明细表状态
        CareOrderItem careOrderItem = new CareOrderItem();
        careOrderItem.setStatus(Constants.ORDER_DETAILS_STATUS_1);
        QueryWrapper<CareOrderItem> wrapper2 = new QueryWrapper<>();
        wrapper2.in(CareOrderItem.COL_ITEM_ID,allItemIds);
        careOrderItemMapper.update(careOrderItem,wrapper2);
    }

    @Override
    public OrderCharge queryOrderChargeByOrderId(String orderId) {
        return orderChargeMapper.selectById(orderId);
    }

    @Override
    public DataGridView queryAllOrderChargeForPage(OrderChargeDto orderChargeDto) {
        Page<OrderCharge> page = new Page<>(orderChargeDto.getPageNum(), orderChargeDto.getPageSize());
        QueryWrapper<OrderCharge> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(orderChargeDto.getRegId()),OrderCharge.COL_REG_ID,
                orderChargeDto.getRegId());
        wrapper.like(StringUtils.isNotBlank(orderChargeDto.getPatientName()),OrderCharge.COL_PATIENT_NAME,
                orderChargeDto.getPatientName());
        orderChargeMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId) {
        QueryWrapper<OrderChargeItem> wrapper = new QueryWrapper<>();
        wrapper.eq(OrderChargeItem.COL_ORDER_ID,orderId);
        return orderChargeItemMapper.selectList(wrapper);
    }

    @Override
    public OrderChargeItem queryOrderChargeItemByItemId(String itemId) {
        return orderChargeItemMapper.selectById(itemId);
    }

    @Override
    public void cancelPayOrder(String orderId) {
        QueryWrapper<OrderCharge> wrapper = new QueryWrapper<>();
        wrapper.eq(OrderCharge.COL_ORDER_STATUS,Constants.ORDER_STATUS_0);
        wrapper.eq(OrderCharge.COL_ORDER_ID,orderId);
        OrderCharge orderCharge = orderChargeMapper.selectOne(wrapper);
        if(orderCharge != null){
            orderCharge.setUpdateTime(DateUtil.date());
            orderCharge.setOrderStatus(Constants.ORDER_STATUS_2);   //支付超时
            orderChargeMapper.updateById(orderCharge);
        }
    }
}




