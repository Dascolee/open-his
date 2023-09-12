package com.dasco.openhis.controller.system;

import com.dasco.openhis.dto.RegisteredItemDto;
import com.dasco.openhis.service.RegisteredItemService;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("system/registeredItem")
public class RegisteredItemController {

    @Autowired
    private RegisteredItemService registeredItemService;

    /**
     * 分页查询挂号费用设置
     * @param registeredItemDto
     * @return
     */
    @GetMapping("listRegisteredItemForPage")
    public AjaxResult listRegisteredItemForPage(RegisteredItemDto registeredItemDto){
        DataGridView dataGridView = registeredItemService.listRegisteredItemForPage(registeredItemDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加挂号项目
     * @param registeredItemDto
     * @return
     */
    @PostMapping("addRegisteredItem")
    public AjaxResult addRegisteredItem(@Validated RegisteredItemDto registeredItemDto){
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(registeredItemService.addRegisteredItem(registeredItemDto));
    }

    /**
     * 修改挂号项目
     * @param registeredItemDto
     * @return
     */
    @PutMapping("updateRegisteredItem")
    public AjaxResult updateRegisteredItem(@Validated RegisteredItemDto registeredItemDto){
        registeredItemDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(registeredItemService.updateRegisteredItem(registeredItemDto));
    }

    /**
     * 根据挂号项目id查询
     * @param registeredItemId
     * @return
     */
    @GetMapping("getRegisteredItemById/{registeredItemId}")
    public AjaxResult getRegisteredItemById(@PathVariable @Validated
                                            @NotNull(message = "挂号项目id不能为空") Long registeredItemId){
        return AjaxResult.success(registeredItemService.getOne(registeredItemId));
    }

    /**
     * 批量删除挂号项目
     * @param registeredItemIds
     * @return
     */
    @DeleteMapping("deleteRegisteredItemByIds/{registeredItemIds}")
    public AjaxResult deleteRegisteredItemByIds(@PathVariable @Validated
                                                @NotEmpty(message = "要删除的id不能为空") Long[] registeredItemIds){
        return AjaxResult.toAjax(registeredItemService.deleteRegisteredItemByIds(registeredItemIds));
    }

    /**
     * 查询所有可用的挂号项目
     */
    @GetMapping("selectAllRegisteredItem")
    public AjaxResult selectAllRegisteredItem() {
        return AjaxResult.success(this.registeredItemService.selectAllRegisteredItem());
    }

}
