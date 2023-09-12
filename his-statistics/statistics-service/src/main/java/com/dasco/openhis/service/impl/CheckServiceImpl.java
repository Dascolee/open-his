package com.dasco.openhis.service.impl;

import com.dasco.openhis.domain.Check;
import com.dasco.openhis.domain.CheckStat;
import com.dasco.openhis.dto.CheckQueryDto;
import com.dasco.openhis.mapper.CheckMapper;
import com.dasco.openhis.service.CheckService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private CheckMapper checkMapper;

    @Override
    public List<Check> queryCheck(CheckQueryDto checkQueryDto) {
        return checkMapper.queryCheck(checkQueryDto);
    }

    @Override
    public List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto) {
        return checkMapper.queryCheckStat(checkQueryDto);
    }
}
