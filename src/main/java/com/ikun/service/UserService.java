package com.ikun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ikun.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ikun.model.dto.UserLoginDTO;
import com.ikun.model.dto.UserRegisterDTO;
import com.ikun.model.dto.UserSearchDTO;
import com.ikun.model.dto.UserUpdateDTO;
import com.ikun.model.vo.SafetyUser;
import jakarta.servlet.http.HttpServletRequest;


public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     * @return 用户id
     */
    Long register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 脱敏用户信息
     */

    SafetyUser login(UserLoginDTO userLoginDTO, HttpServletRequest request);

    SafetyUser updateUser(UserUpdateDTO userUpdateDTO);

    Page<SafetyUser> listUser(Integer currentPage, Integer pageSize, UserSearchDTO userSearchDTO);

    SafetyUser getUserById(Long id);

    void deleteUserById(Long id);

    SafetyUser getSafetyUser(User user);

    boolean isAdmin();
}
