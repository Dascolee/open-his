package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Menu;
import com.dasco.openhis.dto.MenuDto;

import java.util.List;

/**
* @author li118
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2023-07-20 20:59:07
*/
public interface MenuService extends IService<Menu> {
    List<Menu> selectMenuTree(boolean isAdmin);
    List<Menu> listAllMenus(MenuDto menuDto);

    List<Long> getMenuIdsByRoleId(Long roleId);

    Menu getOne(Long menuId);

    int addMenu(MenuDto menuDto);

    int updateMenu(MenuDto menuDto);

    boolean hasChildByMenuId(Long menuId);

    int deleteMenuById(Long menuId);
}
