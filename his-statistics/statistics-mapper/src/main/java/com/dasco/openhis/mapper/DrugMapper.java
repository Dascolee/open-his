package com.dasco.openhis.mapper;

import com.dasco.openhis.domain.Drug;
import com.dasco.openhis.domain.DrugStat;
import com.dasco.openhis.dto.DrugQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugMapper {
    List<Drug> queryDrug(@Param("drug") DrugQueryDto drugQueryDto);

    List<DrugStat> queryDrugStat(@Param("drug") DrugQueryDto drugQueryDto);
}
