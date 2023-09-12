package com.dasco.openhis.controller.statistics;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.domain.Workload;
import com.dasco.openhis.domain.WorkloadStat;
import com.dasco.openhis.dto.WorkloadQueryDto;
import com.dasco.openhis.service.WorkloadService;
import com.dasco.openhis.vo.AjaxResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("statistics/workload")
public class StatWorkloadController {

    @Reference
    private WorkloadService workloadService;

    /**
     * 医生工作量统计
     * @param workloadQueryDto
     * @return
     */
    @GetMapping("queryWorkload")
    public AjaxResult queryWorkload(WorkloadQueryDto workloadQueryDto){
        //如果没有传日期参数，默认查当天
        if(workloadQueryDto.getBeginTime() == null){
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<Workload> workloadList = workloadService.queryWorkload(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }

    /**
     * 总体工作量统计
     * @param workloadQueryDto
     * @return
     */
    @GetMapping("queryWorkloadStat")
    public AjaxResult queryWorkloadStat(WorkloadQueryDto workloadQueryDto){
        //如果没有传日期参数，默认查当天
        if(workloadQueryDto.getBeginTime() == null){
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(),"yyyy-MM-dd"));
        }
        List<WorkloadStat> workloadStatList = workloadService.queryWorkloadStat(workloadQueryDto);
        return AjaxResult.success(workloadStatList);
    }
}
