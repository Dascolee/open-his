package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.service.MenuService;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.Menu;
import com.dasco.openhis.dto.MenuDto;
import com.dasco.openhis.mapper.RoleMapper;
import com.dasco.openhis.mapper.MenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author a
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Menu> selectMenuTree(boolean isAdmin) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.eq(Menu.COL_STATUS, Constants.STATUS_TRUE);
        wrapper.in(Menu.COL_MENU_TYPE,Constants.MENU_TYPE_C,Constants.MENU_TYPE_M,
                Constants.MENU_TYPE_F);
        wrapper.orderByAsc(Menu.COL_PARENT_ID);
        if(isAdmin){
            return menuMapper.selectList(wrapper);
        }else{
            return menuMapper.selectList(wrapper);
        }
    }

    @Override
    public List<Menu> listAllMenus(MenuDto menuDto) {
        QueryWrapper<Menu> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(menuDto.getMenuName()),Menu.COL_MENU_NAME,
                menuDto.getMenuName());
        wrapper.eq(StringUtils.isNotBlank(menuDto.getStatus()),Menu.COL_STATUS,
                menuDto.getStatus());
        return menuMapper.selectList(wrapper);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return menuMapper.queryMenuIdsByRoleId(roleId);
    }

    @Override
    public Menu getOne(Long menuId) {
        return menuMapper.selectById(menuId);
    }

    @Override
    public int addMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        menu.setCreateBy(menuDto.getSimpleUser().getUserName());
        menu.setCreateTime(DateUtil.date());
        return menuMapper.insert(menu);
    }

    @Override
    public int updateMenu(MenuDto menuDto) {
        Menu menu = new Menu();
        BeanUtil.copyProperties(menuDto,menu);
        menu.setUpdateBy(menuDto.getSimpleUser().getUserName());
        return menuMapper.updateById(menu);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        Long count = menuMapper.queryChildCountByMenuId(menuId);
        return count > 0?true:false;
    }

    @Override
    public int deleteMenuById(Long menuId) {
        //删除sys_role_menu表中的数据
        roleMapper.deleteRoleMenuByMenuIds(Arrays.asList(menuId));
        //删除sys_menu表中的数据
        return menuMapper.deleteById(menuId);
    }
}




