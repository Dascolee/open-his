package com.dasco.openhis.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.dto.RevenueQueryDto;
import com.dasco.openhis.service.RevenueService;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("statistics/revenue")
public class RevenueController {

    @Reference
    private RevenueService revenueService;

    /**
     * 根据日期范围查询收支情况
     * @param revenueQueryDto
     * @return
     */
    @GetMapping("queryAllRevenueData")
    public AjaxResult queryAllRevenueData(RevenueQueryDto revenueQueryDto){
        //如果没有传日期参数，默认查当天
        if(revenueQueryDto.getBeginTime() == null){
            revenueQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        Map<String,Object> res = revenueService.queryAllRevenueData(revenueQueryDto);
        return AjaxResult.success(res);
    }
}
