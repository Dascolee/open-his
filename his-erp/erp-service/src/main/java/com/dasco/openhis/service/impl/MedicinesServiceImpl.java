package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.Medicines;
import com.dasco.openhis.dto.MedicinesDto;
import com.dasco.openhis.mapper.MedicinesMapper;
import com.dasco.openhis.service.MedicinesService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
* @author a
* @description 针对表【stock_medicines(药品信息表)】的数据库操作Service实现
*/
@Service
public class MedicinesServiceImpl extends ServiceImpl<MedicinesMapper, Medicines> implements MedicinesService {

    @Autowired
    private MedicinesMapper medicinesMapper;


    @Override
    public DataGridView listMedicinesForPage(MedicinesDto medicinesDto) {
        Page<Medicines> page = new Page<>(medicinesDto.getPageNum(), medicinesDto.getPageSize());
        QueryWrapper<Medicines> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(medicinesDto.getMedicinesName()),Medicines.COL_MEDICINES_NAME,
                medicinesDto.getMedicinesName());
        wrapper.like(StringUtils.isNotBlank(medicinesDto.getKeywords()),Medicines.COL_KEYWORDS,
                medicinesDto.getKeywords());
        wrapper.eq(StringUtils.isNotBlank(medicinesDto.getMedicinesType()),Medicines.COL_MEDICINES_TYPE,
                medicinesDto.getMedicinesType());
        wrapper.eq(StringUtils.isNotBlank(medicinesDto.getProducterId()),Medicines.COL_PRODUCTER_ID,
                medicinesDto.getProducterId());
        wrapper.eq(StringUtils.isNotBlank(medicinesDto.getPrescriptionType()),Medicines.COL_PRESCRIPTION_TYPE,
                medicinesDto.getPrescriptionType());
        wrapper.eq(StringUtils.isNotBlank(medicinesDto.getStatus()),Medicines.COL_STATUS,
                medicinesDto.getStatus());
        medicinesMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addMedicines(MedicinesDto medicinesDto) {
        Medicines medicines = new Medicines();
        BeanUtil.copyProperties(medicinesDto,medicines);
        medicines.setCreateBy(medicinesDto.getSimpleUser().getUserName());
        medicines.setCreateTime(DateUtil.date());
        return medicinesMapper.insert(medicines);
    }

    @Override
    public int updateMedicines(MedicinesDto medicinesDto) {
        Medicines medicines = new Medicines();
        BeanUtil.copyProperties(medicinesDto,medicines);
        medicines.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        return medicinesMapper.updateById(medicines);
    }

    @Override
    public Medicines getOne(Long medicinesId) {
        return medicinesMapper.selectById(medicinesId);
    }

    @Override
    public int deleteMedicinesByIds(Long[] medicinesIds) {
        List<Long> ids = Arrays.asList(medicinesIds);
        if(ids != null && ids.size() > 0){
            return medicinesMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public int updateMedicinesStockNum(Long medicinesId, Integer stockNum) {
        Medicines medicines = new Medicines();
        medicines.setMedicinesId(medicinesId);
        medicines.setMedicinesStockNum(stockNum);
        return medicinesMapper.updateById(medicines);
    }

    /**
     * 发药扣减库存
     * @param medicineId
     * @param num
     * @return
     */
    @Override
    public int deductionMedicinesStorage(Long medicineId, long num) {
        return medicinesMapper.deductionMedicinesStorage(medicineId,num);
    }
}




