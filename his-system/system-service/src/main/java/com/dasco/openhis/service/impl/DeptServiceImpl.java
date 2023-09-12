package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.Dept;
import com.dasco.openhis.dto.DeptDto;
import com.dasco.openhis.mapper.DeptMapper;
import com.dasco.openhis.service.DeptService;
import com.dasco.openhis.vo.DataGridView;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* @author a
* @description 针对表【sys_dept(部门/科室表)】的数据库操作Service实现
*/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Resource
    private DeptMapper deptMapper;

    @Override
    public DataGridView listDeptForPage(DeptDto deptDto) {
        Page<Dept> page = new Page<>(deptDto.getPageNum(), deptDto.getPageSize());
        QueryWrapper<Dept> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(deptDto.getDeptName()),Dept.COL_DEPT_NAME,
                deptDto.getDeptName());
        wrapper.eq(StringUtils.isNotBlank(deptDto.getStatus()),Dept.COL_STATUS,
                deptDto.getStatus());
        wrapper.ge(deptDto.getBeginTime() != null,Dept.COL_CREATE_TIME,
                deptDto.getBeginTime());
        wrapper.le(deptDto.getEndTime() != null,Dept.COL_CREATE_TIME,
                deptDto.getEndTime());
        wrapper.orderByAsc(Dept.COL_ORDER_NUM);
        deptMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());

    }

    @Override
    public int addDept(DeptDto deptDto) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        dept.setCreateBy(deptDto.getSimpleUser().getUserName());
        dept.setCreateTime(DateUtil.date());
        return deptMapper.insert(dept);
    }

    @Override
    public int updateDept(DeptDto deptDto) {
        Dept dept = new Dept();
        BeanUtil.copyProperties(deptDto,dept);
        dept.setUpdateBy(deptDto.getSimpleUser().getUserName());
        return deptMapper.updateById(dept);
    }

    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        List<Long> ids = Arrays.asList(deptIds);
        if(ids != null && ids.size() > 0){
            return deptMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Dept> listDeptByDeptIds(List<Long> deptIds) {
        if(deptIds != null && deptIds.size() > 0){
            QueryWrapper<Dept> wrapper = new QueryWrapper<>();
            wrapper.in(Dept.COL_DEPT_ID,deptIds);
            return deptMapper.selectList(wrapper);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Dept getOne(Long deptId) {
        return deptMapper.selectById(deptId);
    }

    /**
     * 更新科室挂号编号
     * @param deptId
     * @param i
     */
    @Override
    public void updateDeptRegNumber(Long deptId, int i) {
        Dept dept = new Dept();
        dept.setDeptId(deptId);
        dept.setRegNumber(i);
        deptMapper.updateById(dept);
    }
}




