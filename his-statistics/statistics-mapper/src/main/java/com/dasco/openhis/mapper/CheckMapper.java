package com.dasco.openhis.mapper;

import com.dasco.openhis.domain.Check;
import com.dasco.openhis.domain.CheckStat;
import com.dasco.openhis.dto.CheckQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckMapper {
    List<Check> queryCheck(@Param("check") CheckQueryDto checkQueryDto);

    List<CheckStat> queryCheckStat(@Param("check") CheckQueryDto checkQueryDto);
}
