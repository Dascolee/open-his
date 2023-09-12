package com.dasco.openhis.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dasco.openhis.constants.Constants;
import com.dasco.openhis.domain.User;
import com.dasco.openhis.mapper.UserMapper;
import com.dasco.openhis.dto.UserDto;
import com.dasco.openhis.service.UserService;
import com.dasco.openhis.utils.AppMd5Utils;
import com.dasco.openhis.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author a
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(User.COL_PHONE,phone);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public DataGridView listUserForPage(UserDto userDto) {
        Page<User> page = new Page<>(userDto.getPageNum(), userDto.getPageSize());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(userDto.getUserName()),User.COL_USER_NAME,
                userDto.getUserName());
        wrapper.like(StringUtils.isNotBlank(userDto.getPhone()),User.COL_PHONE,
                userDto.getPhone());
        wrapper.eq(StringUtils.isNotBlank(userDto.getStatus()),User.COL_STATUS,
                userDto.getStatus());
        wrapper.eq(userDto.getDeptId() != null,User.COL_DEPT_ID,
                userDto.getDeptId());
        wrapper.ge(userDto.getBeginTime() != null,User.COL_CREATE_TIME,
                userDto.getBeginTime());
        wrapper.ge(userDto.getEndTime() != null,User.COL_CREATE_TIME,
                userDto.getEndTime());
        wrapper.orderByAsc(User.COL_USER_ID);
        userMapper.selectPage(page,wrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addUser(UserDto userDto) {
        User user = new User();
        BeanUtil.copyProperties(userDto,user);
        user.setUserType(Constants.USER_NORMAL);
        String defaultPwd = user.getPhone().substring(5);
        user.setCreateBy(userDto.getSimpleUser().getUserName());
        user.setCreateTime(DateUtil.date());
        user.setSalt(AppMd5Utils.md5(defaultPwd,user.getSalt(),2));
        return userMapper.insert(user);
    }

    @Override
    public int updateUser(UserDto userDto) {
        User user = userMapper.selectById(userDto.getUserId());
        if(user == null){
            return 0;
        }
        BeanUtil.copyProperties(userDto,user);
        user.setUpdateBy(userDto.getSimpleUser().getUserName());
        return userMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        List<Long> ids = Arrays.asList(userIds);
        if(ids != null && ids.size() > 0){
            return userMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public void resetPassword(Long[] userIds) {
        for (Long userId : userIds) {
            User user = userMapper.selectById(userId);
            String defaultPwd = "";
            if(user.getUserType().equals(Constants.USER_ADMIN)){
                defaultPwd = "123456";
            }else{
                defaultPwd = user.getPhone().substring(5);
            }
            user.setSalt(AppMd5Utils.createSalt());
            user.setPassword(AppMd5Utils.md5(defaultPwd,user.getSalt(),2));
            this.userMapper.updateById(user);
        }
    }

    @Override
    public List<User> queryUsersNeedScheduling(Long userId,Long deptId) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(deptId != null,User.COL_DEPT_ID,deptId);
        wrapper.eq(userId != null,User.COL_USER_ID,userId);
        wrapper.eq(User.COL_SCHEDULING_FLAG,Constants.SCHEDULING_FLAG_TRUE);    //0-需要排班
        wrapper.eq(User.COL_STATUS,Constants.STATUS_TRUE);  //0-正常用户
        return userMapper.selectList(wrapper);
    }

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> qw=new QueryWrapper<>();
        qw.eq(User.COL_STATUS, Constants.STATUS_TRUE);
        qw.eq(User.COL_USER_TYPE,Constants.USER_NORMAL);
        qw.orderByAsc(User.COL_USER_ID);
        return this.userMapper.selectList(qw);
    }

    @Override
    public User getOne(Long userId) {
        return this.userMapper.selectById(userId);
    }
}




