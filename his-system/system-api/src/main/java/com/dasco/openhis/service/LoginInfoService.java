package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.LoginInfo;
import com.dasco.openhis.dto.LoginInfoDto;
import com.dasco.openhis.vo.DataGridView;

/**
* @author li118
* @description 针对表【sys_login_info(系统访问记录)】的数据库操作Service
* @createDate 2023-07-20 20:59:07
*/
public interface LoginInfoService extends IService<LoginInfo> {

    int insertLoginInfo(LoginInfo loginInfo);

    DataGridView listForPage(LoginInfoDto loginInfoDto);

    int deleteLoginInfoByIds(Long[] infoIds);

    int clearLoginInfo();

}
