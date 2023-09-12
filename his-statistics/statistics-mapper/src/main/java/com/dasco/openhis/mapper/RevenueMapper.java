package com.dasco.openhis.mapper;

import com.dasco.openhis.domain.Income;
import com.dasco.openhis.domain.Refund;
import com.dasco.openhis.dto.RevenueQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RevenueMapper {
    List<Income> queryIncome(@Param("revenue") RevenueQueryDto revenueQueryDto);

    List<Refund> queryRefund(@Param("revenue") RevenueQueryDto revenueQueryDto);
}
