package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.OperLog;
import com.dasco.openhis.dto.OperLogDto;
import com.dasco.openhis.mapper.OperLogMapper;
import com.dasco.openhis.service.OperLogService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* @author li118
* @description 针对表【sys_oper_log(操作日志记录)】的数据库操作Service实现
* @createDate 2023-07-22 13:44:39
*/
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog>
    implements OperLogService{
    @Resource
    private OperLogMapper operLogMapper;

    @Override
    public DataGridView listForPage(OperLogDto operLogDto) {
        Page<OperLog> page = new Page<>(operLogDto.getPageNum(), operLogDto.getPageSize());
        QueryWrapper<OperLog> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(operLogDto.getOperName()),
                OperLog.COL_OPER_NAME,operLogDto.getOperName());
        wrapper.like(StringUtils.isNotBlank(operLogDto.getTitle()),
                OperLog.COL_TITLE,operLogDto.getTitle());
        wrapper.eq(StringUtils.isNotBlank(operLogDto.getBusinessType()),
                OperLog.COL_BUSINESS_TYPE,operLogDto.getBusinessType());
        wrapper.eq(StringUtils.isNotBlank(operLogDto.getStatus()),
                OperLog.COL_STATUS,operLogDto.getStatus());
        wrapper.ge(operLogDto.getBeginTime() != null, OperLog.COL_OPER_TIME,
                operLogDto.getBeginTime());
        wrapper.le(operLogDto.getEndTime() != null,OperLog.COL_OPER_TIME,
                operLogDto.getEndTime());
        wrapper.orderByDesc(OperLog.COL_OPER_TIME);
        this.operLogMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int deleteOperLogByIds(Long[] infoIds) {
        List<Long> ids = Arrays.asList(infoIds);
        if(ids != null && ids.size() > 0){
            return operLogMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public int clearAllOperLog() {
        return operLogMapper.delete(null);
    }

    @Override
    public int insertOperLog(OperLog operLog) {
        return operLogMapper.insert(operLog);
    }
}




