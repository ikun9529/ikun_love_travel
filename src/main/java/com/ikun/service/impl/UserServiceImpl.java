package com.ikun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ikun.common.constant.Code;
import com.ikun.common.constant.MessageConstant;
import com.ikun.common.constant.UserConstant;
import com.ikun.exception.BussinessException;
import com.ikun.model.domain.User;
import com.ikun.model.domain.UserHolder;
import com.ikun.model.dto.UserLoginDTO;
import com.ikun.model.dto.UserRegisterDTO;
import com.ikun.model.dto.UserSearchDTO;
import com.ikun.model.dto.UserUpdateDTO;
import com.ikun.model.vo.SafetyUser;
import com.ikun.service.UserService;
import com.ikun.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final String SALT = "ikun";
    @Resource
    private UserMapper userMapper;

    @Override
    public Long register(UserRegisterDTO userRegisterDTO) {
        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BussinessException(Code.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BussinessException(Code.PARAMS_ERROR, "账户长度不能小于4");
        }
        if (userPassword.length() < 4 || checkPassword.length() < 4) {
            throw new BussinessException(Code.PARAMS_ERROR, "密码长度不能小于4");
        }
        //账户不能包含字符
        //账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BussinessException(Code.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        //密码和确认密码必须一致
        if (!userPassword.equals(checkPassword)) {
            throw new BussinessException(Code.PARAMS_ERROR, "密码和确认密码必须一致");
        }
        //账户不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        long count = userMapper.selectCount(userQueryWrapper);
        if (count > 0) {
            throw new BussinessException(Code.PARAMS_ERROR, "账户已存在");
        }
        //加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        int result = userMapper.insert(user);
        if (result < 1) {
            throw new BussinessException(Code.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public SafetyUser login(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BussinessException(Code.PARAMS_ERROR, "参数不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BussinessException(Code.PARAMS_ERROR, "账户长度不能小于4");
        }
        if (userPassword.length() < 4) {
            throw new BussinessException(Code.PARAMS_ERROR, "密码长度不能小于4");
        }

        //账户不能包含字符
        //账户不能包含特殊字符
        String validPattern = "^[a-zA-Z0-9_]+$";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (!matcher.find()) {
            throw new BussinessException(Code.PARAMS_ERROR, "账户不能包含特殊字符");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        userQueryWrapper.eq("user_password", encryptPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BussinessException(Code.PARAMS_ERROR, "账户或密码错误");
        }
        //用户脱敏
        SafetyUser safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATUS, user);
        //保存到holder
        UserHolder.saveUser(user);
        return safetyUser;
    }

    @Override
    public SafetyUser updateUser(UserUpdateDTO userUpdateDTO) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        boolean result = this.updateById(user);
        if (!result) {
            throw new BussinessException(Code.SYSTEM_ERROR, MessageConstant.SYSTEM_ERROR);
        }
        return this.getSafetyUser(user);
    }

    @Override
    public Page<SafetyUser> listUser(Integer currentPage, Integer pageSize, UserSearchDTO userSearchDTO) {
        if (!isAdmin()) {
            throw new BussinessException(Code.NO_AUTH, MessageConstant.NO_AUTH);
        }
        //按照userSearchDTO中的条件进行模糊查询
        Page<User> page = new Page<>(currentPage, pageSize);
        Page<User> userPage = this.baseMapper.selectPage(page, null);
        Page<SafetyUser> safetyUserPage = new Page<>();
        safetyUserPage.setTotal(userPage.getTotal());
        safetyUserPage.setCurrent(userPage.getCurrent());
        safetyUserPage.setSize(userPage.getSize());
        safetyUserPage.setRecords(userPage.getRecords().stream()
                .map(this::getSafetyUser)
                .collect(Collectors.toList()));
        return safetyUserPage;
    }

    @Override
    public SafetyUser getUserById(Long id) {
        if (!isAdmin()) {
            throw new BussinessException(Code.NO_AUTH, MessageConstant.NO_AUTH);
        }
        User user = this.getById(id);
        return this.getSafetyUser(user);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!isAdmin()) {
            throw new BussinessException(Code.NO_AUTH, MessageConstant.NO_AUTH);
        }
        boolean result = this.removeById(id);
        if (!result) {
            throw new BussinessException(Code.SYSTEM_ERROR, MessageConstant.SYSTEM_ERROR);
        }
    }

    @Override
    public SafetyUser getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        return SafetyUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userAccount(user.getUserAccount())
                .avatarUrl(user.getAvatarUrl())
                .userRole(user.getUserRole())
                .build();
    }

    @Override
    public boolean isAdmin() {
        Long userId = UserHolder.getUser().getId();
        User user = this.getById(userId);
        return user.getUserRole() == 1;
    }
}




