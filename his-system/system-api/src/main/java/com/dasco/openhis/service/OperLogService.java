package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.OperLog;
import com.dasco.openhis.dto.OperLogDto;
import com.dasco.openhis.vo.DataGridView;

/**
* @author li118
* @description 针对表【sys_oper_log(操作日志记录)】的数据库操作Service
* @createDate 2023-07-22 13:44:39
*/
public interface OperLogService extends IService<OperLog> {

    DataGridView listForPage(OperLogDto operLogDto);

    int deleteOperLogByIds(Long[] infoIds);

    int clearAllOperLog();

    int insertOperLog(OperLog operLog);

}
