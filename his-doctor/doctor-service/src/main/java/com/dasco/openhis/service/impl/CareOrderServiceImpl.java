package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.CareOrder;
import com.dasco.openhis.mapper.CareOrderMapper;
import com.dasco.openhis.service.CareOrderService;
import org.springframework.stereotype.Service;

/**
* @author a
* @description 针对表【his_care_order(药用处方表)】的数据库操作Service实现
*/
@Service
public class CareOrderServiceImpl extends ServiceImpl<CareOrderMapper, CareOrder>
    implements CareOrderService{

}




