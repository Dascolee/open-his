package com.dasco.openhis.service.impl;

import com.dasco.openhis.domain.Drug;
import com.dasco.openhis.domain.DrugStat;
import com.dasco.openhis.dto.DrugQueryDto;
import com.dasco.openhis.mapper.DrugMapper;
import com.dasco.openhis.service.DrugService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class DrugServiceImpl implements DrugService {

    @Autowired
    private DrugMapper drugMapper;

    @Override
    public List<Drug> queryDrug(DrugQueryDto drugQueryDto) {
        return drugMapper.queryDrug(drugQueryDto);
    }

    @Override
    public List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto) {
        return drugMapper.queryDrugStat(drugQueryDto);
    }
}
