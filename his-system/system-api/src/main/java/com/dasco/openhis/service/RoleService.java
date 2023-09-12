package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.Role;
import com.dasco.openhis.dto.RoleDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;

/**
* @author a
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
*/
public interface RoleService extends IService<Role> {

    int saveRoleUser(Long userId,Long[] roleIds);

    List<Role> listAllRoles();

    List<Long> getRoleIdsByUserId(Long userId);

    DataGridView listRoleForPage(RoleDto roleDto);

    int addRole(RoleDto roleDto);

    int updateRole(RoleDto roleDto);

    Role getOne(Long roleId);

    int deleteRoleByIds(Long[] roleIds);

    void saveRoleMenu(Long roleId, Long[] menuIds);
}
