package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.LoginInfo;
import com.dasco.openhis.dto.LoginInfoDto;
import com.dasco.openhis.mapper.LoginInfoMapper;
import com.dasco.openhis.service.LoginInfoService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* @author li118
* @description 针对表【sys_login_info(系统访问记录)】的数据库操作Service实现
* @createDate 2023-07-20 20:59:07
*/
@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo>
    implements LoginInfoService{


    @Resource
    private LoginInfoMapper loginInfoMapper;


    @Override
    public int insertLoginInfo(LoginInfo loginInfo) {
        return loginInfoMapper.insert(loginInfo);
    }


    @Override
    public DataGridView listForPage(LoginInfoDto loginInfoDto) {
        Page<LoginInfo> page = new Page<>(loginInfoDto.getPageNum(), loginInfoDto.getPageSize());
        QueryWrapper<LoginInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(loginInfoDto.getUserName()),
                LoginInfo.COL_USER_NAME,loginInfoDto.getUserName());
        wrapper.like(StringUtils.isNotBlank(loginInfoDto.getIpAddr()),
                LoginInfo.COL_IP_ADDR,loginInfoDto.getIpAddr());
        wrapper.like(StringUtils.isNotBlank(loginInfoDto.getLoginAccount()),
                LoginInfo.COL_LOGIN_ACCOUNT,loginInfoDto.getLoginAccount());
        wrapper.eq(StringUtils.isNotBlank(loginInfoDto.getLoginStatus()),
                LoginInfo.COL_LOGIN_STATUS,loginInfoDto.getLoginStatus());
        wrapper.eq(StringUtils.isNotBlank(loginInfoDto.getLoginType()),
                LoginInfo.COL_LOGIN_TYPE,loginInfoDto.getLoginType());
        wrapper.ge(loginInfoDto.getBeginTime() != null,LoginInfo.COL_LOGIN_TIME,
                loginInfoDto.getBeginTime());
        wrapper.le(loginInfoDto.getEndTime() != null,LoginInfo.COL_LOGIN_TIME,
                loginInfoDto.getEndTime());
        wrapper.orderByDesc(LoginInfo.COL_LOGIN_TIME);
        loginInfoMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int deleteLoginInfoByIds(Long[] infoIds) {
        List<Long> ids = Arrays.asList(infoIds);
        if(ids != null && ids.size() > 0){
            return this.loginInfoMapper.deleteBatchIds(ids);
        }else{
            return 0;
        }
    }

    @Override
    public int clearLoginInfo() {
        return loginInfoMapper.delete(null);
    }
}




