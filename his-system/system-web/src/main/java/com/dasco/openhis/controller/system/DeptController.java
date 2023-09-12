package com.dasco.openhis.controller.system;

import com.dasco.openhis.domain.Dept;
import com.dasco.openhis.dto.DeptDto;
import com.dasco.openhis.service.DeptService;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("system/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 分页查询科室
     * @param deptDto
     * @return
     */
    @GetMapping("listDeptForPage")
    public AjaxResult listDeptForPage(DeptDto deptDto){
        DataGridView dataGridView = deptService.listDeptForPage(deptDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加科室
     * @param deptDto
     * @return
     */
    @PostMapping("addDept")
    public AjaxResult addDept(@Validated DeptDto deptDto){
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(deptService.addDept(deptDto));
    }

    /**
     * 修改科室
     * @param deptDto
     * @return
     */
    @PutMapping("updateDept")
    public AjaxResult updateDept(@Validated DeptDto deptDto){
        deptDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(deptService.updateDept(deptDto));
    }

    /**
     * 批量删除部门
     * @param deptIds
     * @return
     */
    @DeleteMapping("deleteDeptByIds/{deptIds}")
    public AjaxResult deleteDeptByIds(@PathVariable @Validated
                                      @NotEmpty(message = "要删除的id不能为空") Long[] deptIds){
        return AjaxResult.toAjax(deptService.deleteDeptByIds(deptIds));
    }

    /**
     * 查询所有可用的科室
     */
    @GetMapping("selectAllDept")
    public AjaxResult selectAllDept(){
        return AjaxResult.success(this.deptService.list());
    }

    /**
     *
     * @param deptId
     * @return
     */
    @GetMapping("getDeptById/{deptId}")
    public AjaxResult getDeptById(@PathVariable @Validated @NotEmpty(message = "科室 ID 为空") Long deptId) {
        Dept dept = this.deptService.getOne(deptId);
        return AjaxResult.success(dept);
    }

}
