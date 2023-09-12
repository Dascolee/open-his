package com.dasco.openhis.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.dasco.openhis.config.pay.PayService;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.*;
import com.dasco.openhis.dto.OrderChargeDto;
import com.dasco.openhis.dto.OrderChargeFromDto;
import com.dasco.openhis.dto.OrderChargeItemDto;
import com.dasco.openhis.service.CareHistoryService;
import com.dasco.openhis.service.OrderChargeService;
import com.dasco.openhis.utils.IdGeneratorSnowFlake;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("doctor/charge")
public class OrderChargeController {

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private OrderChargeService orderChargeService;

    /**
     * 根据挂号单id查询未支付的处方及详情
     * @param regId
     * @return
     */
    @GetMapping("getNoChargeCareHistoryByRegId/{regId}")
    public AjaxResult getNoChargeCareHistoryByRegId(@PathVariable String regId){
        CareHistory careHistory = careHistoryService.queryCareHistoryByRegId(regId);
        if(careHistory == null){
            return AjaxResult.fail("【"+regId+"】对应的病历不存在，请核对后再查询");
        }
        HashMap<String,Object> res = new HashMap<>();
        res.put("careHistory",careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        //定义一个存放careorders的集合
        ArrayList<Map<String,Object>> mapList = new ArrayList<>();
        //根据病历编号查处方
        List<CareOrder> careOrders = careHistoryService.queryCareOrdersByChId(careHistory.getChId());
        if(careOrders.isEmpty()){
           return AjaxResult.fail("【"+regId+"】没有相应的处方信息，请核对后再查询");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
            BigDecimal allAmount = new BigDecimal("0");
            //根据处方id查处方详情
            List<CareOrderItem> careOrderItems = careHistoryService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_STATUS_0);
            //如果当前处方未支付的详情为空，结束本次循环
            if(careOrderItems.isEmpty()){
                continue;
            }else{
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount = allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("allAmount",allAmount);
                beanToMap.put("careOrderItems",careOrderItems);
                mapList.add(beanToMap);
            }
        }
        if(mapList.isEmpty()){
            return AjaxResult.fail("【"+regId+"】挂号单没有未支付的处方信息，请核对后再查询");
        }else{
            res.put("careOrders",mapList);
            return AjaxResult.success(res);
        }
    }

    /**
     * 创建现金订单
     * @param orderChargeFromDto
     * @return
     */
    @PostMapping("createOrderChargeWithCash")
    public AjaxResult createOrderChargeWithCash(@RequestBody @Validated OrderChargeFromDto orderChargeFromDto){
        //1保存订单
        orderChargeFromDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_0);    //现金支付
        orderChargeFromDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        String orderId = IdGeneratorSnowFlake.generatorIdWithProfix(Constants.ID_PROFIX_ODC);
        orderChargeFromDto.getOrderChargeDto().setOrderId(orderId);
        orderChargeService.saveOrderAndItems(orderChargeFromDto);
        //2更新详情状态
        orderChargeService.paySuccess(orderId,null,Constants.PAY_TYPE_0);
        return AjaxResult.success("创建现金订单并支付成功");
    }

    /**
     * 创建支付宝订单
     * @param orderChargeFromDto
     * @return
     */
    @PostMapping("createOrderChargeWithZfb")
    public AjaxResult createOrderChargeWithZfb(@RequestBody @Validated OrderChargeFromDto orderChargeFromDto){
        //1保存订单
        orderChargeFromDto.getOrderChargeDto().setPayType(Constants.PAY_TYPE_1);    //支付宝支付
        orderChargeFromDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        String orderId = IdGeneratorSnowFlake.generatorIdWithProfix(Constants.ID_PROFIX_ODC);
        orderChargeFromDto.getOrderChargeDto().setOrderId(orderId);
        orderChargeService.saveOrderAndItems(orderChargeFromDto);
        //2进行支付宝支付，给页面返回一个二维码
        String outTradeNo = orderId;
        String subject = "OPEN-HIS医疗管理系统支付平台";
        String totalAmount=orderChargeFromDto.getOrderChargeDto().getOrderAmount().toString();
        String undiscountableAmount=null;
        String body="";
        List<OrderChargeItemDto> itemDtoList = orderChargeFromDto.getOrderChargeItemDtoList();
        for (OrderChargeItemDto orderChargeItemDto : itemDtoList) {
            body += orderChargeItemDto.getItemName() + "-" + orderChargeItemDto.getItemPrice() + " ";
        }
        String notifyUrl="http://45314rh250.qicp.vip/pay/callback/"+outTradeNo;
        Map<String, Object> map = PayService.pay(outTradeNo, subject, totalAmount, undiscountableAmount, body, notifyUrl);
        String qrCode = map.get("qrCode").toString();
        if(StringUtils.isNotBlank(qrCode)){
            //支付成功
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("orderId",orderId);
            map1.put("allAmount",totalAmount);
            map1.put("payUrl",qrCode);
            return AjaxResult.success(map1);
        }else{
            return AjaxResult.fail(map.get("msg").toString());
        }
    }

    /**
     * 根据订单id查询订单信息（验证用户是否支付成功）
     * @param orderId
     * @return
     */
    @GetMapping("queryOrderChargeOrderId/{orderId}")
    public AjaxResult queryOrderChargeOrderId(@PathVariable String orderId){
        OrderCharge orderCharge = orderChargeService.queryOrderChargeByOrderId(orderId);
        if(orderCharge == null){
            return AjaxResult.fail("【"+orderId+"】所在的订单号不存在，请核对后再输入");
        }
        return AjaxResult.success(orderCharge);
    }

    /**
     * 分页查询收费列表
     * @param orderChargeDto
     * @return
     */
    @GetMapping("queryAllOrderChargeForPage")
    public AjaxResult queryAllOrderChargeForPage(OrderChargeDto orderChargeDto){
        DataGridView dataGridView = orderChargeService.queryAllOrderChargeForPage(orderChargeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    @GetMapping("queryOrderChargeItemByOrderId/{orderId}")
    public AjaxResult queryOrderChargeItemByOrderId(@PathVariable String orderId){
        List<OrderChargeItem> list = orderChargeService.queryOrderChargeItemByOrderId(orderId);
        return AjaxResult.success(list);
    }

    /**
     * 收费列表-现金支付
     * @param orderId
     * @return
     */
    @GetMapping("payWithCash/{orderId}")
    public AjaxResult payWithCash(@PathVariable String orderId){
        OrderCharge orderCharge = orderChargeService.queryOrderChargeByOrderId(orderId);
        if(orderCharge == null){
            return AjaxResult.fail("【"+orderId+"】对应的订单不存在，请核对后再输入");
        }
        if(orderCharge.getOrderStatus().equals(Constants.ORDER_STATUS_1)){
            return AjaxResult.fail("【"+orderId+"】对应的订单是已支付状态，请核对后再输入");
        }
        orderChargeService.paySuccess(orderId,null,Constants.PAY_TYPE_0);
        return AjaxResult.success();
    }

    /**
     * 收费列表-支付宝支付
     * @param orderId
     * @return
     */
    @GetMapping("toPayOrderWithZfb/{orderId}")
    public AjaxResult toPayOrderWithZfb(@PathVariable String orderId){
        OrderCharge orderCharge = orderChargeService.queryOrderChargeByOrderId(orderId);
        if(orderCharge == null){
            return AjaxResult.fail("【"+orderId+"】对应的订单不存在，请核对后再输入");
        }
        if(orderCharge.getOrderStatus().equals(Constants.ORDER_STATUS_1)){
            return AjaxResult.fail("【"+orderId+"】对应的订单是已支付状态，请核对后再输入");
        }
        //支付宝支付
        String outTradeNo = orderId;
        String subject = "OPEN-HIS医疗管理系统支付平台";
        String totalAmount=orderCharge.getOrderAmount().toString();
        String undiscountableAmount=null;
        String body = "";
        String notifyUrl="http://45314rh250.qicp.vip/pay/callback/"+outTradeNo;
        Map<String, Object> map = PayService.pay(outTradeNo, subject, totalAmount, undiscountableAmount, body, notifyUrl);
        String qrCode = map.get("qrCode").toString();
        if(StringUtils.isNotBlank(qrCode)){
            //支付成功
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("orderId",orderId);
            map1.put("allAmount",totalAmount);
            map1.put("payUrl",qrCode);
            return AjaxResult.success(map1);
        }else{
            return AjaxResult.fail(map.get("msg").toString());
        }
    }
}
