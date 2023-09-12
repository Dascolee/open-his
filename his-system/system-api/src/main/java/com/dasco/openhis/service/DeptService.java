package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Dept;
import com.dasco.openhis.dto.DeptDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【sys_dept(部门/科室表)】的数据库操作Service
*/
public interface DeptService extends IService<Dept> {

    DataGridView listDeptForPage(DeptDto deptDto);

    int addDept(DeptDto deptDto);

    int updateDept(DeptDto deptDto);

    int deleteDeptByIds(Long[] deptIds);

    List<Dept> listDeptByDeptIds(List<Long> deptIds);

    Dept getOne(Long deptId);

    void updateDeptRegNumber(Long deptId, int i);
}
