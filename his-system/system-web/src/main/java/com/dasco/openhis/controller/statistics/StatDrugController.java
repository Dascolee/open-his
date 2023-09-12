package com.dasco.openhis.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.domain.Drug;
import com.dasco.openhis.domain.DrugStat;
import com.dasco.openhis.dto.DrugQueryDto;
import com.dasco.openhis.service.DrugService;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/drug")
public class StatDrugController {

    @Reference
    private DrugService drugService;

    /**
     * 药品销售列表
     * @param drugQueryDto
     * @return
     */
    @GetMapping("queryDrug")
    public AjaxResult queryDrug(DrugQueryDto drugQueryDto){
        if(drugQueryDto.getBeginTime() == null){
            drugQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<Drug> drugList = drugService.queryDrug(drugQueryDto);
        return AjaxResult.success(drugList);
    }

    /**
     * 发药数量统计
     * @param drugQueryDto
     * @return
     */
    @GetMapping("queryDrugStat")
    public AjaxResult queryDrugStat(DrugQueryDto drugQueryDto){
        if(drugQueryDto.getBeginTime() == null){
            drugQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<DrugStat> drugStatList = drugService.queryDrugStat(drugQueryDto);
        return AjaxResult.success(drugStatList);
    }

}
