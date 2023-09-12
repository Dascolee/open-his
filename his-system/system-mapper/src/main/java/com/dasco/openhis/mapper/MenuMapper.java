package com.dasco.openhis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dasco.openhis.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author li118
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2023-07-20 20:59:07
* @Entity com.dasco.openhis.domain.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);

    Long queryChildCountByMenuId(@Param("menuId") Long menuId);
}




