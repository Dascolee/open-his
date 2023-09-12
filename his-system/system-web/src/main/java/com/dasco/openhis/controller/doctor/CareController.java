package com.dasco.openhis.controller.doctor;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.*;
import com.dasco.openhis.dto.CareHistoryDto;
import com.dasco.openhis.dto.CareOrderFormDto;
import com.dasco.openhis.service.CareHistoryService;
import com.dasco.openhis.service.DeptService;
import com.dasco.openhis.service.PatientService;
import com.dasco.openhis.service.RegistrationService;
import com.dasco.openhis.utils.HisDateUtils;
import com.dasco.openhis.utils.IdGeneratorSnowFlake;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("doctor/care")
public class CareController {

    @Reference
    private PatientService patientService;

    @Reference
    private RegistrationService registrationService;

    @Reference
    private CareHistoryService careHistoryService;

    @Autowired
    private DeptService deptService;

    /**
     * 查询待就诊挂号信息
     * @param schedulingType
     * @return
     */
    @GetMapping("queryToBeSeenRegistration/{schedulingType}")
    public AjaxResult queryToBeSeenRegistration(@PathVariable String schedulingType){
        //获取部门id
        Long deptId = ShiroSecurityUtils.getCurrentActiveUser().getUser().getDeptId();
        //设置挂号信息的状态
        String regStatus = Constants.REG_STATUS_1;
        //计算时间段
        String subSectionType = HisDateUtils.getCurrentTimeType();
        //查询
        Long userId = null;
        List<Registration> list = registrationService.queryRegistration(deptId,subSectionType,schedulingType,regStatus,userId);
        return AjaxResult.success(list);
    }

    /**
     * 查询就诊中挂号信息
     * @param schedulingType
     * @return
     */
    @GetMapping("queryVisitingRegistration/{schedulingType}")
    public AjaxResult queryVisitingRegistration(@PathVariable String schedulingType){
        //获取部门id
        Long deptId = ShiroSecurityUtils.getCurrentActiveUser().getUser().getDeptId();
        //设置挂号信息的状态
        String regStatus = Constants.REG_STATUS_2;
        //查询
        Long userId = ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserId();
        List<Registration> list = registrationService.queryRegistration(deptId,null,schedulingType,regStatus,userId);
        return AjaxResult.success(list);
    }

    /**
     * 查询就诊完成挂号信息
     * @param schedulingType
     * @return
     */
    @GetMapping("queryVisitCompletedRegistration/{schedulingType}")
    public AjaxResult queryVisitCompletedRegistration(@PathVariable String schedulingType){
        //获取部门id
        Long deptId = ShiroSecurityUtils.getCurrentActiveUser().getUser().getDeptId();
        //设置挂号信息的状态
        String regStatus = Constants.REG_STATUS_3;
        //查询
        Long userId = ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserId();
        List<Registration> list = registrationService.queryRegistration(deptId,null,schedulingType,regStatus,userId);
        return AjaxResult.success(list);
    }

    /**
     * 接诊
     * @param regId
     * @return
     */
    @PostMapping("receivePatient/{regId}")
    public AjaxResult receivePatient(@PathVariable String regId){
        Registration registration = registrationService.queryRegistrationByRegId(regId);
        if(registration == null){
            return AjaxResult.fail("【"+regId+"】挂号单不存在，不能接诊");
        }
        //判断挂号单的状态，必须是待就诊才能接诊
        if(registration.getRegStatus().equals(Constants.REG_STATUS_1)){
            registration.setRegStatus(Constants.REG_STATUS_2);
            registration.setUserId(ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserId());
            registration.setDoctorName(ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserName());
            return AjaxResult.toAjax(registrationService.updateRegistrationByRegId(registration));
        }else{
            return AjaxResult.fail("【"+regId+"】挂号单的状态不是待就诊，不能接诊");
        }
    }

    /**
     * 根据患者id查询患者信息\档案信息\病历信息
     * @param patientId
     * @return
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId){
        //查询患者信息
        Patient patient = patientService.getPatientById(patientId);
        //查询患者档案信息
        PatientFile patientFile = patientService.getPatientFileById(patientId);
        //查询患者病历信息
        List<CareHistory> careHistories = careHistoryService.queryCareHistoryByPatientId(patientId);
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("patient",patient);
        resMap.put("patientFile",patientFile);
        resMap.put("careHistoryList",careHistories);
        return AjaxResult.success(resMap);
    }

    /**
     * 保存病历
     * @param careHistoryDto
     * @return
     */
    @PostMapping("saveCareHistory")
    public AjaxResult saveCareHistory(@RequestBody CareHistoryDto careHistoryDto){
        careHistoryDto.setUserId(ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserId());
        careHistoryDto.setUserName(ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserName());
        careHistoryDto.setDeptId(ShiroSecurityUtils.getCurrentActiveUser().getUser().getDeptId());
        Dept dept = deptService.getOne(ShiroSecurityUtils.getCurrentActiveUser().getUser().getDeptId());
        careHistoryDto.setDeptName(dept.getDeptName());
        careHistoryDto.setCareDate(DateUtil.date());
        CareHistory careHistory = careHistoryService.saveOrUpdateCareHistory(careHistoryDto);
        return AjaxResult.success(careHistory);
    }

    /**
     * 根据挂号id查询病历信息
     * @param regId
     * @return
     */
    @GetMapping("getCareHistoryByRegId/{regId}")
    public AjaxResult getCareHistoryByRegId(@PathVariable String regId){
        CareHistory careHistory = careHistoryService.queryCareHistoryByRegId(regId);
        return AjaxResult.success(careHistory);
    }

    /**
     * 根据病历id查询处方及处方详情
     * @param chId
     * @return
     */
    @GetMapping("queryCareOrdersByChId/{chId}")
    public AjaxResult queryCareOrderByChId(@PathVariable String chId){
        //根据病历id查询处方表
        List<CareOrder> careOrders = careHistoryService.queryCareOrdersByChId(chId);
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        //遍历处方集合，查询对应的处方详情
        for (CareOrder careOrder : careOrders) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("careOrder",careOrder);
            List<CareOrderItem> items = careHistoryService.queryCareOrderItemsByCoId(careOrder.getCoId(),null);
            map.put("careOrderItems",items);
            list.add(map);
        }
        return AjaxResult.success(list);
    }

    /**
     * 保存处方及详情
     * @param careOrderFormDto
     * @return
     */
    @PostMapping("saveCareOrderItem")
    public AjaxResult saveCareOrderItem(@RequestBody @Validated CareOrderFormDto careOrderFormDto){
        CareHistory careHistory = careHistoryService.queryCareHistoryByChId(careOrderFormDto.getCareOrder().getChId());
        if(careHistory == null){
            return AjaxResult.fail("病历ID【"+careHistory.getChId()+ "】不存在，请核对后再提交");
        }
        careOrderFormDto.getCareOrder().setCoId(
                IdGeneratorSnowFlake.generatorIdWithProfix(Constants.ID_PROFIX_CO));
        careOrderFormDto.getCareOrder().setPatientId(careHistory.getPatientId());
        careOrderFormDto.getCareOrder().setPatientName(careHistory.getPatientName());
        careOrderFormDto.getCareOrder().setUserId(ShiroSecurityUtils.getCurrentActiveUser().getUser().getUserId());
        careOrderFormDto.getCareOrder().setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(careHistoryService.saveCareOrderItem(careOrderFormDto));
    }

    /**
     * 根据处方详情id删除详情数据（未支付）
     * @param itemId
     * @return
     */
    @DeleteMapping("deleteCareOrderItemById/{itemId}")
    public AjaxResult deleteCareOrderItemById(@PathVariable String itemId){
        CareOrderItem careOrderItem = careHistoryService.queryCareOrderItemsByItemId(itemId);
        if(careOrderItem == null){
            return AjaxResult.fail("处方详情不存在");
        }
        if(!careOrderItem.getStatus().equals(Constants.ORDER_DETAILS_STATUS_0)){
            return AjaxResult.fail("【"+itemId+"】不是未支付状态，不能删除");
        }
        return AjaxResult.toAjax(careHistoryService.deleteCareOrderItemById(itemId));
    }

    /**
     * 完成就诊
     * @param regId
     * @return
     */
    @PostMapping("visitComplete/{regId}")
    public AjaxResult visitComplete(@PathVariable String regId){
        Registration registration = registrationService.queryRegistrationByRegId(regId);
        if(registration == null){
            return AjaxResult.fail("【"+regId+"】挂号单不存在，请核对后再提交");
        }
        if(!registration.getRegStatus().equals(Constants.REG_STATUS_2)){
            return AjaxResult.fail("【"+regId+"】挂号单不是就诊中状态，不能完成就诊");
        }
        //更新挂号单状态
        return AjaxResult.toAjax(careHistoryService.visitComplete(regId));
    }
}
