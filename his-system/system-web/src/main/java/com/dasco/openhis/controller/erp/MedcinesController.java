package com.dasco.openhis.controller.erp;

import com.dasco.openhis.service.MedicinesService;
import com.dasco.openhis.dto.MedicinesDto;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("erp/medicines")
public class MedcinesController {

    @Reference
    private MedicinesService medicinesService;

    /**
     * 分页查询药品
     * @param medicinesDto
     * @return
     */
    @GetMapping("listMedicinesForPage")
    public AjaxResult listMedicinesForPage(MedicinesDto medicinesDto){
        DataGridView dataGridView = medicinesService.listMedicinesForPage(medicinesDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加药品
     * @param medicinesDto
     * @return
     */
    @PostMapping("addMedicines")
    public AjaxResult addMedicines(@Validated MedicinesDto medicinesDto){
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(medicinesService.addMedicines(medicinesDto));
    }

    /**
     * 修改药品
     * @param medicinesDto
     * @return
     */
    @PostMapping("updateMedicines")
    public AjaxResult updateMedicines(@Validated MedicinesDto medicinesDto){
        medicinesDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(medicinesService.updateMedicines(medicinesDto));
    }

    /**
     * 根据id查询药品
     * @param medicinesId
     * @return
     */
    @GetMapping("getMedicinesById/{medicinesId}")
    public AjaxResult getMedicinesById(@PathVariable @Validated @NotNull(message = "药品id不能为空") Long medicinesId){
        return AjaxResult.success(medicinesService.getOne(medicinesId));
    }

    /**
     * 批量删除药品
     * @param medicinesIds
     * @return
     */
    @DeleteMapping("deleteMedicinesByIds/{medicinesIds}")
    public AjaxResult deleteMedicinesByIds(@PathVariable @Validated @NotEmpty(message = "要删除的id不能为空") Long[] medicinesIds){
        return AjaxResult.toAjax(medicinesService.deleteMedicinesByIds(medicinesIds));
    }

    /**
     * 更新库存
     * @param medicinesId
     * @param stockNum
     * @return
     */
    @PostMapping("updateMedicinesStorage/{medicinesId}/{stockNum}")
    public AjaxResult updateMedicinesStockNum(@PathVariable Long medicinesId,@PathVariable Integer stockNum){
        int i = medicinesService.updateMedicinesStockNum(medicinesId,stockNum);
        return AjaxResult.toAjax(i);
    }
}
