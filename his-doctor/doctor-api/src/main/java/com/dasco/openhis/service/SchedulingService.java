package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Scheduling;
import com.dasco.openhis.dto.SchedulingFormDto;
import com.dasco.openhis.dto.SchedulingQueryDto;

import java.util.List;

/**
* @author a
* @description 针对表【his_scheduling(排班信息表)】的数据库操作Service
*/
public interface SchedulingService extends IService<Scheduling> {

    List<Scheduling> queryScheduling(SchedulingQueryDto schedulingQueryDto);

    int saveScheduling(SchedulingFormDto schedulingFormDto);

    List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType);
}
