package com.dasco.openhis.service;

import com.dasco.openhis.domain.Check;
import com.dasco.openhis.domain.CheckStat;
import com.dasco.openhis.dto.CheckQueryDto;

import java.util.List;

public interface CheckService {
    List<Check> queryCheck(CheckQueryDto checkQueryDto);

    List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto);
}
