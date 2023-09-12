package com.dasco.openhis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.domain.RoleUser;
import com.dasco.openhis.mapper.RoleUserMapper;
import com.dasco.openhis.service.RoleUserService;
import org.springframework.stereotype.Service;

/**
* @author a
* @description 针对表【sys_role_user(用户和角色关联表)】的数据库操作Service实现
*/
@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser>
    implements RoleUserService {

}




