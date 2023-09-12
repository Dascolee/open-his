package com.dasco.openhis.service;

import com.dasco.openhis.domain.Workload;
import com.dasco.openhis.domain.WorkloadStat;
import com.dasco.openhis.dto.WorkloadQueryDto;

import java.util.List;

public interface WorkloadService {
    List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto);

    List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto);
}
