package com.dasco.openhis.controller.doctor;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.CareHistory;
import com.dasco.openhis.domain.CareOrder;
import com.dasco.openhis.domain.CareOrderItem;
import com.dasco.openhis.domain.CheckResult;
import com.dasco.openhis.dto.CheckResultDto;
import com.dasco.openhis.dto.CheckResultFormDto;
import com.dasco.openhis.service.CareHistoryService;
import com.dasco.openhis.service.CheckResultService;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("doctor/check")
public class CheckResultController {

    @Reference
    private CareHistoryService careHistoryService;

    @Reference
    private CheckResultService checkResultService;

    /**
     * 根据挂号ID查询已支付的检查处方及详情
     * @param checkResultDto
     * @return
     */
    @PostMapping("queryNeedCheckItem")
    public AjaxResult queryNeedCheckItem(@RequestBody CheckResultDto checkResultDto){
        ArrayList<CareOrderItem> list = new ArrayList<>();
        if(StringUtils.isNotBlank(checkResultDto.getRegId())){
            //根据挂号单号查询病例信息
            CareHistory careHistory = careHistoryService.queryCareHistoryByRegId(checkResultDto.getRegId());
            if(careHistory == null){
                return AjaxResult.success(list);
            }
            //查询挂号单下对应的处方集合
            List<CareOrder> careOrders = careHistoryService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder careOrder : careOrders) {
                if(careOrder.getCoType().equals(Constants.CO_TYPE_CHECK)){
                    List<CareOrderItem> careOrderItems = careHistoryService.queryCareOrderItemsByCoId(careOrder.getCoId(), Constants.ORDER_DETAILS_STATUS_1);
                    for (CareOrderItem careOrderItem : careOrderItems) {
                        if(checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))){
                            list.add(careOrderItem);
                        }
                    }
                }
            }
            return AjaxResult.success(list);
        }else{
            //查询所有病人的检查处方明细(已支付的)
            List<CareOrderItem> careOrderItems = careHistoryService.queryCareOrderItemsByStatus(Constants.CO_TYPE_CHECK,Constants.ORDER_DETAILS_STATUS_1);
            for (CareOrderItem careOrderItem : careOrderItems) {
                if(checkResultDto.getCheckItemIds().contains(careOrderItem.getItemRefId())){
                    list.add(careOrderItem);
                }
            }
        }
        return AjaxResult.success(list);
    }

    /**
     * 根据检查单号查询要检查的项目详情
     * @param itemId
     * @return
     */
    @GetMapping("queryCheckItemByItemId/{itemId}")
    public AjaxResult queryCheckItemByItemId(@PathVariable String itemId){
        CareOrderItem careOrderItem = careHistoryService.queryCareOrderItemsByItemId(itemId);
        if(careOrderItem == null){
            return AjaxResult.fail("【"+itemId+"】检查单数据不存在，请核对后再查");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)){
            return AjaxResult.fail("【"+itemId+"】检查单没有支付，请支付后再查");
        }
        if(!careOrderItem.getItemType().equals(Constants.CO_TYPE_CHECK)){
            return AjaxResult.fail("【"+itemId+"】检查单不是检查类处方，请核对后再查");
        }
        CareOrder careOrder = careHistoryService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = careHistoryService.queryCareHistoryByChId(careOrder.getChId());
        HashMap<String,Object> map = new HashMap<>();
        map.put("item",careOrderItem);
        map.put("careOrder",careOrder);
        map.put("careHistory",careHistory);
        return AjaxResult.success(map);
    }

    /**
     * 开始检查
     * @param itemId
     * @return
     */
    @PostMapping("startCheck/{itemId}")
    public AjaxResult startCheck(@PathVariable String itemId){
        CareOrderItem careOrderItem = careHistoryService.queryCareOrderItemsByItemId(itemId);
        if(careOrderItem == null){
            return AjaxResult.fail("【"+itemId+"】检查单数据不存在，请核对后再查");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_1)){
            return AjaxResult.fail("【"+itemId+"】检查单没有支付，请支付后再查");
        }
        if(!careOrderItem.getItemType().equals(Constants.CO_TYPE_CHECK)){
            return AjaxResult.fail("【"+itemId+"】检查单不是检查类处方，请核对后再查");
        }
        //查询处方
        CareOrder careOrder = careHistoryService.queryCareOrderByCoId(careOrderItem.getCoId());
        //查询病例
        CareHistory careHistory = careHistoryService.queryCareHistoryByChId(careOrder.getChId());
        CheckResult checkResult = new CheckResult();
        checkResult.setItemId(itemId);
        checkResult.setCheckItemId(Integer.valueOf(careOrderItem.getItemRefId()));
        checkResult.setCheckItemName(careOrderItem.getItemName());
        checkResult.setPatientId(careOrder.getPatientId());
        checkResult.setPatientName(careOrder.getPatientName());
        checkResult.setPrice(careOrderItem.getPrice());
        checkResult.setRegId(careHistory.getRegId());
        checkResult.setResultStatus(Constants.RESULT_STATUS_0);
        checkResult.setCreateTime(DateUtil.date());
        checkResult.setCreateBy(ShiroSecurityUtils.getCurrentSimpleUser().getUserName());
        return AjaxResult.toAjax(checkResultService.saveCheckResult(checkResult));
    }

    /**
     * 查询所有检查中的项目
     * @param checkResultDto
     * @return
     */
    @PostMapping("queryAllCheckingResultForPage")
    public AjaxResult queryAllCheckingResultForPage(@RequestBody CheckResultDto checkResultDto){
        checkResultDto.setResultStatus(Constants.RESULT_STATUS_0);
        DataGridView dataGridView = checkResultService.queryAllCheckingResultForPage(checkResultDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 结束检查
     * @param checkResultFormDto
     * @return
     */
    @PostMapping("completeCheckResult")
    public AjaxResult completeCheck(@RequestBody @Validated CheckResultFormDto checkResultFormDto){
        checkResultFormDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(checkResultService.completeCheck(checkResultFormDto));
    }

    /**
     * 查询所有检查中和检查完成的项目
     * @param checkResultDto
     * @return
     */
    @PostMapping("queryAllCheckResultForPage")
    public AjaxResult queryAllCheckResultForPage(@RequestBody CheckResultDto checkResultDto){
        DataGridView dataGridView = checkResultService.queryAllCheckResultForPage(checkResultDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }
}
