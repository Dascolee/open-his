package com.dasco.openhis.service.impl;

import com.dasco.openhis.domain.Workload;
import com.dasco.openhis.domain.WorkloadStat;
import com.dasco.openhis.dto.WorkloadQueryDto;
import com.dasco.openhis.mapper.WorkloadMapper;
import com.dasco.openhis.service.WorkloadService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class WorkloadServiceImpl implements WorkloadService {

    @Autowired
    private WorkloadMapper workloadMapper;

    @Override
    public List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto) {
        return workloadMapper.queryWorkload(workloadQueryDto);
    }

    @Override
    public List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        return workloadMapper.queryWorkloadStat(workloadQueryDto);
    }
}
