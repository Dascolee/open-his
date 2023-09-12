package com.dasco.openhis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dasco.openhis.domain.Scheduling;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author a
* @description 针对表【his_scheduling(排班信息表)】的数据库操作Mapper
* @Entity com.itbaizhan.openhis.domain.Scheduling
*/
public interface SchedulingMapper extends BaseMapper<Scheduling> {

    List<Long> queryHasSchedulingDeptIds(@Param("deptId") Long deptId,
                                         @Param("schedulingDay") String schedulingDay,
                                         @Param("schedulingType") String schedulingType,
                                         @Param("subsectionType") String subsectionType);
}




