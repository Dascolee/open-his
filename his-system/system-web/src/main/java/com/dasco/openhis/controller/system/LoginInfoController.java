package com.dasco.openhis.controller.system;


import com.dasco.openhis.dto.LoginInfoDto;
import com.dasco.openhis.service.LoginInfoService;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/loginInfo")
public class LoginInfoController {

    @Autowired
    private LoginInfoService loginInfoService;

    /**
     * 分页查询登录日志
     * @param loginInfoDto
     * @return
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(LoginInfoDto loginInfoDto){
        DataGridView dataGridView = loginInfoService.listForPage(loginInfoDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 批量删除登录日志
     * @param infoIds
     * @return
     */
    @DeleteMapping("deleteLoginInfoByIds/{infoIds}")
    public AjaxResult deleteLoginInfoByIds(@PathVariable Long[] infoIds){
        return AjaxResult.toAjax(loginInfoService.deleteLoginInfoByIds(infoIds));
    }

    /**
     * 清空登录日志
     * @return
     */
    @DeleteMapping("clearLoginInfo")
    public AjaxResult clearLoginInfo(){
        return AjaxResult.toAjax(loginInfoService.clearLoginInfo());
    }
}
