package com.dasco.openhis.controller.system;

import com.dasco.openhis.dto.DictTypeDto;
import com.dasco.openhis.service.DictTypeService;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("system/dict/type")
public class DictTypeController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 分页查询字典类型
     * @param dictTypeDto
     * @return
     */
    @GetMapping("listForPage")
    private AjaxResult listForPage(DictTypeDto dictTypeDto){
        DataGridView dataGridView = dictTypeService.listForPage(dictTypeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping("addDictType")
    public AjaxResult addDictType(@Validated DictTypeDto dictTypeDto) {
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("新增字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.insert(dictTypeDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateDictType")
    public AjaxResult updateDictType(@Validated DictTypeDto dictTypeDto) {
        if (dictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("修改字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.dictTypeService.update(dictTypeDto));
    }


    /**
     * 根据ID查询一个字典信息
     */
    @GetMapping("getOne/{dictId}")
    public AjaxResult getDictType(@PathVariable @Validated @NotNull(message = "字典ID不能为空") Long dictId) {
        return AjaxResult.success(this.dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteDictTypeByIds/{dictIds}")
    public AjaxResult updateDictType(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] dictIds) {
        return AjaxResult.toAjax(this.dictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("selectAllDictType")
    public AjaxResult selectAllDictType(){
        return AjaxResult.success(this.dictTypeService.listAll().getData());
    }

    @GetMapping("dictCacheAsync")
    public AjaxResult dictCacheAsync(){
        dictTypeService.dictCacheAsync();
        return AjaxResult.success();
    }
}
