package com.dasco.openhis.controller.doctor;

import cn.hutool.core.bean.BeanUtil;
import com.dasco.openhis.domain.*;
import com.dasco.openhis.dto.PatientDto;
import com.dasco.openhis.service.CareHistoryService;
import com.dasco.openhis.service.PatientService;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("doctor/patient")
public class PatientController {

    @Reference
    private PatientService patientService;

    @Reference
    private CareHistoryService careHistoryService;

    /**
     * 分页查询患者信息
     * @param patientDto
     * @return
     */
    @GetMapping("listPatientForPage")
    public AjaxResult listPatientForPage(PatientDto patientDto){
        DataGridView dataGridView = patientService.listPatientForPage(patientDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据患者id查询患者信息
     * @param patientId
     * @return
     */
    @GetMapping("getPatientById/{patientId}")
    public AjaxResult getPatientById(@PathVariable String patientId){
        Patient patient = patientService.getPatientById(patientId);
        return AjaxResult.success(patient);
    }

    /**
     * 根据患者id查询患者档案
     * @param patientId
     * @return
     */
    @GetMapping("getPatientFileById/{patientId}")
    public AjaxResult getPatientFileById(@PathVariable String patientId){
        PatientFile patientFile = patientService.getPatientFileById(patientId);
        return AjaxResult.success(patientFile);
    }

    /**
     * 根据患者id查询患者信息、患者档案、历史病例
     * @param patientId
     * @return
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId){
        //查询患者病例信息
        List<CareHistory> careHistoryList = careHistoryService.queryCareHistoryByPatientId(patientId);
        //构造返回的数据对象
        List<Map<String,Object>> res = new ArrayList<>();
        for (CareHistory careHistory : careHistoryList) {
            Map<String, Object> careHistoryMap = BeanUtil.beanToMap(careHistory);
            careHistoryMap.put("careOrders", Collections.EMPTY_LIST);
            List<Map<String,Object>> reCareOrders = new ArrayList<>();
            //根据病例id查询处方列表
            List<CareOrder> careOrders = careHistoryService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder careOrder : careOrders) {
                //查处方详情
                Map<String, Object> careOrderMap = BeanUtil.beanToMap(careOrder);
                List<CareOrderItem> careOrderItems = careHistoryService.queryCareOrderItemsByCoId(careOrder.getCoId(),null);
                careOrderMap.put("careOrderItems",careOrderItems);
                reCareOrders.add(careOrderMap);
            }
            careHistoryMap.put("careOrders",reCareOrders);
            res.add(careHistoryMap);
        }
        return AjaxResult.success(res);
    }
}
