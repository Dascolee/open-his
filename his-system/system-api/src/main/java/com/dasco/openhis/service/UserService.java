package com.dasco.openhis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dasco.openhis.domain.User;
import com.dasco.openhis.dto.UserDto;
import com.dasco.openhis.vo.DataGridView;

import java.util.List;


/**
* @author li118
* @description 针对表【sys_user(用户信息表)】的数据库操作Service
* @createDate 2023-07-20 12:50:19
*/
public interface UserService extends IService<User> {
    User queryUserByPhone(String phone);

    DataGridView listUserForPage(UserDto userDto);

    int addUser(UserDto userDto);

    int updateUser(UserDto userDto);

    int deleteUserByIds(Long[] userIds);

    void resetPassword(Long[] userIds);

    List<User> queryUsersNeedScheduling(Long userId, Long deptId);

    /**
     * 查询所有可用的用户
     * @return
     */
    List<User> getAllUsers();

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户编号
     * @return
     */
    User getOne(Long userId);
}
