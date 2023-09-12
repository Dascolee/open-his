package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.Role;
import com.dasco.openhis.dto.RoleDto;
import com.dasco.openhis.mapper.RoleMapper;
import com.dasco.openhis.service.RoleService;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* @author a
* @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public int saveRoleUser(Long userId, Long[] roleIds) {
        roleMapper.deleteRoleUserByUserIds(Arrays.asList(userId));
        for (Long roleId : roleIds) {
            this.roleMapper.saveRoleUser(userId,roleId);
        }
        return 0;
    }

    @Override
    public List<Role> listAllRoles() {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq(Role.COL_STATUS, Constants.STATUS_TRUE);
        wrapper.orderByAsc(Role.COL_ROLE_SORT);
        return roleMapper.selectList(wrapper);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return roleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public DataGridView listRoleForPage(RoleDto roleDto) {
        Page<Role> page = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(roleDto.getRoleName()),Role.COL_ROLE_NAME,
                roleDto.getRoleName());
        wrapper.like(StringUtils.isNotBlank(roleDto.getRoleCode()),Role.COL_ROLE_CODE,
                roleDto.getRoleCode());
        wrapper.eq(StringUtils.isNotBlank(roleDto.getStatus()),Role.COL_STATUS,
                roleDto.getStatus());
        wrapper.ge(roleDto.getBeginTime() != null,Role.COL_CREATE_TIME,
                roleDto.getBeginTime());
        wrapper.le(roleDto.getEndTime() != null,Role.COL_CREATE_TIME,
                roleDto.getEndTime());
        wrapper.orderByAsc(Role.COL_ROLE_SORT);
        roleMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto,role);
        role.setCreateBy(roleDto.getSimpleUser().getUserName());
        role.setCreateTime(DateUtil.date());
        return roleMapper.insert(role);
    }

    @Override
    public int updateRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtil.copyProperties(roleDto,role);
        role.setUpdateBy(roleDto.getSimpleUser().getUserName());
        return roleMapper.updateById(role);
    }

    @Override
    public Role getOne(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        List<Long> ids = Arrays.asList(roleIds);
        if(ids != null && ids.size() > 0){
            //1、删除sys_role_menu表中的关联数据
            roleMapper.deleteRoleMenuByRoleIds(ids);
            //2、删除sys_role_user表中的关联数据
            roleMapper.deleteRoleUserByRoleIds(ids);
            //3、删除sys_role中的数据
            return roleMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public void saveRoleMenu(Long roleId, Long[] menuIds) {
        //根据角色id先删除sys_role_menu表中的关联数据
        roleMapper.deleteRoleMenuByRoleIds(Arrays.asList(roleId));
        for (Long menuId : menuIds) {
            roleMapper.saveRoleMenu(roleId,menuId);
        }
    }
}




