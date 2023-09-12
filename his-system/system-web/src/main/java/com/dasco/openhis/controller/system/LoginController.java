package com.dasco.openhis.controller.system;

import cn.hutool.core.date.DateUtil;
import com.dasco.openhis.aspectj.Log;
import com.dasco.openhis.aspectj.enums.BusinessType;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.constants.HttpStatus;
import com.dasco.openhis.domain.LoginInfo;
import com.dasco.openhis.domain.Menu;
import com.dasco.openhis.dto.LoginBodyDto;
import com.dasco.openhis.service.LoginInfoService;
import com.dasco.openhis.service.MenuService;
import com.dasco.openhis.utils.AddressUtils;
import com.dasco.openhis.utils.IpUtils;
import com.dasco.openhis.utils.ShiroSecurityUtils;
import com.dasco.openhis.vo.ActiverUser;
import com.dasco.openhis.vo.AjaxResult;
import com.dasco.openhis.vo.MenuTreeVo;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private LoginInfoService loginInfoService;


    @PostMapping("login/doLogin")
    @Log(title = "用户登录",businessType = BusinessType.INSERT)
    public AjaxResult login(@RequestBody @Validated LoginBodyDto loginBodyDto,
                            HttpServletRequest httpServletRequest) {
        AjaxResult ajaxResult = AjaxResult.success();
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        //封装loginInfo对象
        LoginInfo loginInfo = createLoginInfo(httpServletRequest);
        loginInfo.setLoginAccount(loginBodyDto.getUsername());
        try {
            subject.login(token);
            Serializable webToken = subject.getSession().getId();
            ajaxResult.put(Constants.TOKEN,webToken);
            loginInfo.setMsg("登录成功");
            loginInfo.setLoginStatus(Constants.LOGIN_SUCCESS);
            loginInfo.setUserName(ShiroSecurityUtils.getCurrentSimpleUser().getUserName());
        } catch (AuthenticationException e) {
            log.error("用户名或密码不正确");
            ajaxResult = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
            loginInfo.setMsg("用户名或密码不正确");
            loginInfo.setLoginStatus(Constants.LOGIN_ERROR);
        }
        //保存登录信息到数据库
        loginInfoService.insertLoginInfo(loginInfo);
        return ajaxResult;
    }


    /**
     * 创建登录对象
     * @param httpServletRequest
     * @return
     */
    private LoginInfo createLoginInfo(HttpServletRequest httpServletRequest) {
        LoginInfo loginInfo = new LoginInfo();
        UserAgent userAgent = UserAgent.parseUserAgentString(httpServletRequest.getHeader("User-Agent"));
        //获取IP地址
        String ipAddr = IpUtils.getIpAddr(httpServletRequest);
        //获取操作系统
        String osName = userAgent.getOperatingSystem().getName();
        //获取浏览器类型
        String browser = userAgent.getBrowser().getName();
        //获取登录地址
        String location = AddressUtils.getRealAddressByIP(ipAddr);
        //设置IP地址
        loginInfo.setIpAddr(ipAddr);
        //设置登录地址
        loginInfo.setLoginLocation(location);
        //设置操作系统
        loginInfo.setOs(osName);
        //设置浏览器类型
        loginInfo.setBrowser(browser);
        //设置登录时间
        loginInfo.setLoginTime(DateUtil.date());
        //设置登录类型
        loginInfo.setLoginType(Constants.LOGIN_TYPE_SYSTEM);
        return loginInfo;
    }


    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("login/getInfo")
    public AjaxResult getInfo(){
        Subject subject = SecurityUtils.getSubject();
        ActiverUser activerUser = (ActiverUser)subject.getPrincipal();
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("username",activerUser.getUser().getUserName());
        ajaxResult.put("picture",activerUser.getUser().getPicture());
        ajaxResult.put("roles",activerUser.getRoles());
        ajaxResult.put("permissions",activerUser.getPermissions());
        return ajaxResult;
    }

    /**
     * 获取菜单信息
     * @return
     */
    @GetMapping("login/getMenus")
    public AjaxResult getMenus(){
        Subject subject = SecurityUtils.getSubject();
        ActiverUser activerUser = (ActiverUser)subject.getPrincipal();
        boolean isAdmin = activerUser.getUser().getUserType().equals(Constants.USER_ADMIN);
        List<Menu> menus = menuService.selectMenuTree(isAdmin);
        ArrayList<MenuTreeVo> menuVos = new ArrayList<>();
        for (Menu menu : menus) {
            menuVos.add(new MenuTreeVo(menu.getMenuId().toString(),menu.getPath(),true));
        }
        return AjaxResult.success(menuVos);
    }

    @PostMapping("login/logout")
    public AjaxResult logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }
}
