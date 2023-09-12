package com.dasco.openhis.mapper;

import com.dasco.openhis.domain.Workload;
import com.dasco.openhis.domain.WorkloadStat;
import com.dasco.openhis.dto.WorkloadQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkloadMapper {
    List<Workload> queryWorkload(@Param("workload") WorkloadQueryDto workloadQueryDto);

    List<WorkloadStat> queryWorkloadStat(@Param("workload") WorkloadQueryDto workloadQueryDto);
}
