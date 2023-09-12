package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Registration;
import com.dasco.openhis.dto.RegistrationDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【his_registration】的数据库操作Service
*/
public interface RegistrationService extends IService<Registration> {

    void addRegistration(RegistrationDto registrationDto);

    Registration queryRegistrationByRegId(String regId);

    int updateRegistrationByRegId(Registration registration);

    DataGridView queryRegistrationForPage(RegistrationDto registrationDto);

    List<Registration> queryRegistration(Long deptId, String subSectionType, String schedulingType, String regStatus, Long userId);
}
