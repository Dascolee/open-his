package com.dasco.openhis.service;

import com.dasco.openhis.domain.Drug;
import com.dasco.openhis.domain.DrugStat;
import com.dasco.openhis.dto.DrugQueryDto;

import java.util.List;

public interface DrugService {
    List<Drug> queryDrug(DrugQueryDto drugQueryDto);

    List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto);
}
