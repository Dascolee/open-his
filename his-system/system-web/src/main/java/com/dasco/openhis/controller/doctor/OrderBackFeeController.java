package com.dasco.openhis.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.dasco.openhis.config.pay.PayService;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.*;
import com.dasco.openhis.dto.OrderBackfeeDto;
import com.dasco.openhis.dto.OrderBackfeeFormDto;
import com.dasco.openhis.service.CareHistoryService;
import com.dasco.openhis.service.OrderBackfeeService;
import com.dasco.openhis.service.OrderChargeService;
import com.dasco.openhis.utils.IdGeneratorSnowFlake;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("doctor/backfee")
public class OrderBackFeeController {

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private OrderBackfeeService orderBackfeeService;

    @Reference
    private OrderChargeService orderChargeService;

    /**
     * 查询已支付的处方单
     * @param regId
     * @return
     */
    @GetMapping("getChargedCareHistoryByRegId/{regId}")
    public AjaxResult getChargedHistoryByRegId(@PathVariable String regId){
        HashMap<String,Object> res = new HashMap<>();
        CareHistory careHistory = careHistoryService.queryCareHistoryByRegId(regId);
        if(careHistory == null){
            return AjaxResult.fail("【"+regId+"】对应的病例信息不存在，请核对后再输入");
        }
        //放入默认值
        res.put("careHistory",careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        //声明一个可以存放careOrders的集合
        List<Map<String,Object>> mapList = new ArrayList<>();
        List<CareOrder> careOrders = careHistoryService.queryCareOrdersByChId(careHistory.getChId());
        if(careOrders.isEmpty()){
            return AjaxResult.fail("【"+regId+"】挂号单对应的处方信息不存在，请核对后再输入");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
            beanToMap.put("careOrderItems",Collections.EMPTY_LIST);
            BigDecimal allAmount = new BigDecimal("0");
            //根据处方id查询明细信息
            List<CareOrderItem> careOrderItems = careHistoryService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_1);
            if(careOrderItems.isEmpty()){
                continue;
            }else{
                //重新计算金额
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("careOrderItems",careOrderItems);
                beanToMap.put("allAmount",allAmount);
                mapList.add(beanToMap);
            }
        }
        if(mapList.isEmpty()){
            return AjaxResult.fail("【"+regId+"】挂号单没有已支付的处方信息，请核对后再查询");
        }else{
            res.put("careOrders", mapList);
            return AjaxResult.success(res);
        }
    }

    /**
     * 创建现金退费订单
     * @param orderBackfeeFormDto
     * @return
     */
    @PostMapping("createOrderBackfeeWithCash")
    public AjaxResult createOrderBackfeeWithCash(@RequestBody @Validated OrderBackfeeFormDto orderBackfeeFormDto){
        orderBackfeeFormDto.getOrderBackfeeDto().setBackType(Constants.PAY_TYPE_0);
        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        //保存退费单
        String backId = IdGeneratorSnowFlake.generatorIdWithProfix(Constants.ID_PROFIX_ODB);
        orderBackfeeFormDto.getOrderBackfeeDto().setBackId(backId);
        String itemId = orderBackfeeFormDto.getOrderBackfeeItemDtoList().get(0).getItemId();
        OrderChargeItem orderChargeItem = orderChargeService.queryOrderChargeItemByItemId(itemId);
        orderBackfeeFormDto.getOrderBackfeeDto().setOrderId(orderChargeItem.getOrderId());
        orderBackfeeService.saveOrderAndItems(orderBackfeeFormDto);
        //更新退费单状态
        orderBackfeeService.backSuccess(backId,null,Constants.PAY_TYPE_0);
        return AjaxResult.success("创建现金退费单成功");
    }

    /**
     * 创建支付宝退费订单
     * @param orderBackfeeFormDto
     * @return
     */
    @PostMapping("createOrderBackfeeWithZfb")
    public AjaxResult createOrderBackfeeWithZfb(@RequestBody @Validated OrderBackfeeFormDto orderBackfeeFormDto){
        orderBackfeeFormDto.getOrderBackfeeDto().setBackType(Constants.PAY_TYPE_1);
        orderBackfeeFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        //保存退费单
        String backId = IdGeneratorSnowFlake.generatorIdWithProfix(Constants.ID_PROFIX_ODB);
        orderBackfeeFormDto.getOrderBackfeeDto().setBackId(backId);
        String itemId = orderBackfeeFormDto.getOrderBackfeeItemDtoList().get(0).getItemId();
        OrderChargeItem orderChargeItem = orderChargeService.queryOrderChargeItemByItemId(itemId);
        OrderCharge orderCharge = orderChargeService.queryOrderChargeByOrderId(orderChargeItem.getOrderId());
        if(orderCharge == null){
            return AjaxResult.fail("【"+orderCharge.getRegId()+"】没有支付记录，不能进行退费操作");
        }
        if(orderCharge.getPayType().equals(Constants.PAY_TYPE_0)){
            return AjaxResult.fail("【"+orderCharge.getRegId()+"】对应的支付方式不是支付宝，不能进行退费操作");
        }
        orderBackfeeFormDto.getOrderBackfeeDto().setOrderId(orderChargeItem.getOrderId());
        orderBackfeeService.saveOrderAndItems(orderBackfeeFormDto);
        //支付宝退费
        String outTradeNo=orderCharge.getOrderId();
        String tradeNo = orderCharge.getPayPlatformId();
        String refundAmount=orderBackfeeFormDto.getOrderBackfeeDto().getBackAmount().toString();
        String refundReason="不想要了";
        String outRequestNo = backId;
        Map<String, Object> map = PayService.payBack(outTradeNo, tradeNo, refundAmount, refundReason, outRequestNo);
        if(map.get("code").toString().equals("200")){
            orderBackfeeService.backSuccess(backId,map.get("tradeNo").toString(),Constants.PAY_TYPE_1);
            return AjaxResult.success();
        }else{
            return AjaxResult.fail(map.get("msg").toString());
        }
    }

    /**
     * 分页查询退费单
     * @param orderBackfeeDto
     * @return
     */
    @GetMapping("queryAllOrderBackfeeForPage")
    public AjaxResult queryAllOrderBackfeeForPage(OrderBackfeeDto orderBackfeeDto){
        DataGridView dataGridView = orderBackfeeService.queryAllOrderBackfeeForPage(orderBackfeeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 查看退费详情
     * @param backId
     * @return
     */
    @GetMapping("queryOrderBackfeeItemByBackId/{backId}")
    public AjaxResult queryOrderBackfeeItemByBackId(@PathVariable String backId){
        List<OrderBackfeeItem> list = orderBackfeeService.queryOrderBackfeeItemByBackId(backId);
        return AjaxResult.success(list);
    }
}
