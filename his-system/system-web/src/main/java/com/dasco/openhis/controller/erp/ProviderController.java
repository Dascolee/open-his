package com.dasco.openhis.controller.erp;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.dasco.openhis.service.ProviderService;
import com.dasco.openhis.controller.BaseController;
import com.dasco.openhis.dto.ProviderDto;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("erp/provider")
public class  ProviderController {

    @Reference
    private ProviderService providerService;

    /**
     * 分页查询供应商
     * @param providerDto
     * @return
     */
    @GetMapping("listProviderForPage")
    @SentinelResource(value = "listProviderForPage",blockHandlerClass = BaseController.class,blockHandler = "handleException")
    public AjaxResult listProviderForPage(ProviderDto providerDto){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DataGridView dataGridView = providerService.listProviderForPage(providerDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加供应商
     * @param providerDto
     * @return
     */
    @PostMapping("addProvider")
    public AjaxResult addProvider(@Validated ProviderDto providerDto){
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(providerService.addProvider(providerDto));
    }

    /**
     * 修改供应商
     * @param providerDto
     * @return
     */
    @PostMapping("updateProvider")
    public AjaxResult updateProvider(@Validated ProviderDto providerDto){
        providerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(providerService.updateProvider(providerDto));
    }

    /**
     * 根据id查询一个供应商
     * @param providerId
     * @return
     */
    @GetMapping("getProviderById/{providerId}")
    public AjaxResult getProviderById(@PathVariable @Validated @NotNull(message = "供应商id不能为空") Long providerId){
        return AjaxResult.success(providerService.getOne(providerId));
    }

    /**
     * 批量删除供应商
     * @param providerIds
     * @return
     */
    @DeleteMapping("deleteProviderByIds/{providerIds}")
    public AjaxResult deleteProviderByIds(@PathVariable @Validated @NotEmpty(message = "要删除的id不能为空") Long[] providerIds){
        return AjaxResult.toAjax(providerService.deleteProviderByIds(providerIds));
    }

    /**
     * 查询所有可用的供应商
     */
    @GetMapping("selectAllProvider")
    public AjaxResult selectAllProvider() {
        return AjaxResult.success(this.providerService.selectAllProvider());
    }
}
