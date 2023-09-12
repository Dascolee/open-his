package com.dasco.openhis.controller.erp;

import com.dasco.openhis.service.ProducerService;
import com.dasco.openhis.dto.ProducerDto;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("erp/producter")
public class ProducerController {

    @Reference
    private ProducerService producerService;

    /**
     * 分页查询生产厂家
     * @param producerDto
     * @return
     */
    @GetMapping("listProducterForPage")
    public AjaxResult listProducerForPage(ProducerDto producerDto){
        DataGridView dataGridView = producerService.listProducerForPage(producerDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加厂家
     * @param producerDto
     * @return
     */
    @PostMapping("addProducter")
    public AjaxResult addProducer(@Validated ProducerDto producerDto){
        producerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(producerService.addProducer(producerDto));
    }

    /**
     * 修改厂家
     * @param producerDto
     * @return
     */
    @PostMapping("updateProducter")
    public AjaxResult updateProducer(@Validated ProducerDto producerDto){
        producerDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(producerService.updateProducer(producerDto));
    }

    /**
     * 根据厂家id查询
     * @param producerId
     * @return
     */
    @GetMapping("getProducterById/{producerId}")
    public AjaxResult getProducerById(@PathVariable @Validated @NotNull(message = "厂家ID不能为空") Long producerId){
        return AjaxResult.success(producerService.getOne(producerId));
    }

    /**
     * 批量删除厂家
     * @param producerIds
     * @return
     */
    @DeleteMapping("deleteProducterByIds/{producerIds}")
    public AjaxResult deleteProducterByIds(@PathVariable @Validated @NotEmpty(message = "要删除的id不能为空") Long[] producerIds){
        return AjaxResult.toAjax(producerService.deleteProducterByIds(producerIds));
    }

    /**
     * 查询所有可用的生产厂家
     */
    @GetMapping("selectAllProducter")
    public AjaxResult selectAllProducter() {
        return AjaxResult.success(this.producerService.selectAllProducter());
    }

}
