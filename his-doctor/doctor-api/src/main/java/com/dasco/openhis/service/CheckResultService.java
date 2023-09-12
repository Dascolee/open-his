package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.CheckResult;
import com.dasco.openhis.dto.CheckResultDto;
import com.dasco.openhis.dto.CheckResultFormDto;
import com.dasco.openhis.vo.DataGridView;

/**
* @author a
* @description 针对表【his_check_result】的数据库操作Service
*/
public interface CheckResultService extends IService<CheckResult> {

    int saveCheckResult(CheckResult checkResult);

    DataGridView queryAllCheckingResultForPage(CheckResultDto checkResultDto);

    int completeCheck(CheckResultFormDto checkResultFormDto);

    DataGridView queryAllCheckResultForPage(CheckResultDto checkResultDto);
}
