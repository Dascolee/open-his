package com.dasco.openhis.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.domain.Check;
import com.dasco.openhis.domain.CheckStat;
import com.dasco.openhis.dto.CheckQueryDto;
import com.dasco.openhis.service.CheckService;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics/check")
public class CheckController {

    @Reference
    private CheckService checkService;

    /**
     * 检查项目列表
     * @param checkQueryDto
     * @return
     */
    @GetMapping("queryCheck")
    public AjaxResult queryCheck(CheckQueryDto checkQueryDto){
        //如果没有传日期参数，默认查当天
        if(checkQueryDto.getBeginTime() == null){
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<Check> checkList = checkService.queryCheck(checkQueryDto);
        return AjaxResult.success(checkList);
    }

    /**
     * 检查项目统计
     * @param checkQueryDto
     * @return
     */
    @GetMapping("queryCheckStat")
    public AjaxResult queryCheckStat(CheckQueryDto checkQueryDto){
        //如果没有传日期参数，默认查当天
        if(checkQueryDto.getBeginTime() == null){
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<CheckStat> checkStatList = checkService.queryCheckStat(checkQueryDto);
        return AjaxResult.success(checkStatList);
    }
}
