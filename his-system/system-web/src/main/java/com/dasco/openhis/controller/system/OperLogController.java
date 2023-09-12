package com.dasco.openhis.controller.system;

import com.dasco.openhis.dto.OperLogDto;
import com.dasco.openhis.service.OperLogService;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 操作日志controller
 */
@RestController
@RequestMapping("system/operLog")
public class OperLogController {

    @Autowired
    private OperLogService operLogService;

    /**
     * 分页查询操作日志
     * @param operLogDto
     * @return
     */
    @GetMapping("listForPage")
    private AjaxResult listForPage(OperLogDto operLogDto){
        DataGridView dataGridView = operLogService.listForPage(operLogDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 批量删除操作日志
     * @param infoIds
     * @return
     */
    @DeleteMapping("deleteOperLogByIds/{infoIds}")
    private AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds){
        return AjaxResult.toAjax(operLogService.deleteOperLogByIds(infoIds));
    }

    /**
     * 清空日志
     * @return
     */
    @DeleteMapping("clearAllOperLog")
    private AjaxResult clearAllOperLog(){
        return AjaxResult.toAjax(operLogService.clearAllOperLog());
    }
}
