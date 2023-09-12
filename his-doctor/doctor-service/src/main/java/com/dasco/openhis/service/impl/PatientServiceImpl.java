package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.Patient;
import com.dasco.openhis.domain.PatientFile;
import com.dasco.openhis.dto.PatientDto;
import com.dasco.openhis.mapper.PatientFileMapper;
import com.dasco.openhis.mapper.PatientMapper;
import com.dasco.openhis.service.PatientService;
import com.dasco.openhis.utils.AppMd5Utils;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
* @author a
* @description 针对表【his_patient(患者信息表)】的数据库操作Service实现
*/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService{

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientFileMapper patientFileMapper;

    @Override
    public DataGridView listPatientForPage(PatientDto patientDto) {
        Page<Patient> page = new Page<>(patientDto.getPageNum(), patientDto.getPageSize());
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(patientDto.getName()),Patient.COL_NAME,
                patientDto.getName());
        wrapper.like(StringUtils.isNotBlank(patientDto.getPhone()),Patient.COL_PHONE,
                patientDto.getPhone());
        wrapper.like(StringUtils.isNotBlank(patientDto.getIdCard()),Patient.COL_ID_CARD,
                patientDto.getIdCard());
        patientMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public Patient getPatientById(String patientId) {
        Patient patient = patientMapper.selectById(patientId);
        return patient;
    }

    @Override
    public PatientFile getPatientFileById(String patientId) {
        return patientFileMapper.selectById(patientId);
    }

    @Override
    public Patient getPatientByIdCard(String idCard) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq(Patient.COL_ID_CARD,idCard);
        return patientMapper.selectOne(wrapper);
    }

    @Override
    public Patient addPatient(PatientDto patientDto) {
        Patient patient = new Patient();
        BeanUtil.copyProperties(patientDto,patient);
        patient.setCreateTime(DateUtil.date());
        patient.setIsFinal(Constants.IS_FINAL_FALSE);   //默认0--未完善
        String defaultPwd = patient.getPhone().substring(5);
        patient.setPassword(AppMd5Utils.md5(defaultPwd,patient.getPhone(),2));
        patientMapper.insert(patient);
        return patient;
    }
}




