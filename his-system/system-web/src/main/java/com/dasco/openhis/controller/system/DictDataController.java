package com.dasco.openhis.controller.system;

import com.dasco.openhis.dto.DictDataDto;
import com.dasco.openhis.service.DictDataService;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("system/dict/data")
public class DictDataController {

    @Autowired
    private DictDataService dataService;

    /**
     * 查询字典数据列表
     * @param dictDataDto
     * @return
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(DictDataDto dictDataDto){
        DataGridView dataGridView = dataService.listForPage(dictDataDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 根据dictId查询字典数据
     * @param dictId
     * @return
     */
    @GetMapping("getOne/{dictId}")
    public AjaxResult getDictData(@PathVariable @Validated
                                  @NotNull(message = "字典数据ID不能为空") Long dictId){
        return AjaxResult.success(dataService.selectDictDataById(dictId));
    }

    /**
     * 新增字典数据
     * @param dictDataDto
     * @return
     */
    @PostMapping("addDictData")
    public AjaxResult addDictData(@Validated DictDataDto dictDataDto){
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(dataService.insert(dictDataDto));
    }

    /**
     * 修改字典数据
     * @param dictDataDto
     * @return
     */
    @PutMapping("updateDictData")
    public AjaxResult updateDictData(@Validated DictDataDto dictDataDto){
        dictDataDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(dataService.update(dictDataDto));
    }

    /**
     * 删除字典数据
     * @param dictIds
     * @return
     */
    @DeleteMapping("deleteDictDataByIds/{dictIds}")
    public AjaxResult deleteDictData(@PathVariable @Validated
                                     @NotEmpty(message = "要删除的字典id不能为空") Long[] dictIds){
        return AjaxResult.toAjax(dataService.deleteDictData(dictIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("getDataByType/{dictType}")
    public AjaxResult getDataByType(@PathVariable @Validated @NotEmpty(message = "字典类型不能为空")  String dictType){
        return AjaxResult.success(this.dataService.selectDictDataByDictType(dictType));
    }

}
