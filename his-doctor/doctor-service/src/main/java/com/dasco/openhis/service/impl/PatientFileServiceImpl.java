package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.PatientFile;
import com.dasco.openhis.mapper.PatientFileMapper;
import com.dasco.openhis.service.PatientFileService;
import org.springframework.stereotype.Service;

/**
* @author a
* @description 针对表【his_patient_file】的数据库操作Service实现
*/
@Service
public class PatientFileServiceImpl extends ServiceImpl<PatientFileMapper, PatientFile>
    implements PatientFileService{

}




