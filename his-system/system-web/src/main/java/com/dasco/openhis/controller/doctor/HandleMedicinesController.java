package com.dasco.openhis.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.CareHistory;
import com.dasco.openhis.domain.CareOrder;
import com.dasco.openhis.domain.CareOrderItem;
import com.dasco.openhis.service.CareHistoryService;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("doctor/handleMedicine")
public class HandleMedicinesController {

    @Reference
    private CareHistoryService careHistoryService;

    /**
     * 查询已支付的处方单
     * @param regId
     * @return
     */
    @GetMapping("getChargedCareHistoryOnlyMedicinesByRegId/{regId}")
    public AjaxResult getChargedCareHistoryOnlyMedicinesByRegId(@PathVariable String regId){
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
            if(careOrder.getCoType().equals(Constants.CO_TYPE_MEDICINES)){
                Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
                BigDecimal allAmount = new BigDecimal("0");
                //根据处方id查处方详情
                List<CareOrderItem> careOrderItems = careHistoryService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_STATUS_1);
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
        }
        if(mapList.isEmpty()){
            return AjaxResult.fail("【"+regId+"】挂号单没有已支付的药品处方信息，请核对后再查询");
        }else{
            res.put("careOrders",mapList);
            return AjaxResult.success(res);
        }
    }

    /**
     * 发药
     * @param itemIds
     * @return
     */
    @PostMapping("doMedicine")
    public AjaxResult doMedicine(@RequestBody List<String> itemIds){
        if(itemIds == null || itemIds.isEmpty()){
            return AjaxResult.fail("请选择要发药的药品项");
        }
        String msg = careHistoryService.doMedicine(itemIds);
        if(StringUtils.isNotBlank(msg)){
            return AjaxResult.fail(msg);
        }else{
            return AjaxResult.success();
        }
    }
}
