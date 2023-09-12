package com.dasco.openhis.service;

import com.dasco.openhis.dto.RevenueQueryDto;

import java.util.Map;

public interface RevenueService {
    Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto);
}
