package com.ikun.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ikun.common.Result;
import com.ikun.model.dto.UserLoginDTO;
import com.ikun.model.dto.UserRegisterDTO;
import com.ikun.model.dto.UserSearchDTO;
import com.ikun.model.dto.UserUpdateDTO;
import com.ikun.model.vo.SafetyUser;
import com.ikun.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "用户模块")
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     * @return 用户id
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Long> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        Long userId = userService.register(userRegisterDTO);
        return Result.success(userId);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<SafetyUser> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        SafetyUser safetyUser = userService.login(userLoginDTO, request);
        return Result.success(safetyUser);
    }

    /**
     * 用户登出
     *
     * @return 成功
     */
    @GetMapping("/logout")
    @Operation(summary = "用户登出")
    public Result logout() {
        return Result.success();
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户信息")
    public Result<SafetyUser> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        SafetyUser safetyUser = userService.updateUser(userUpdateDTO);
        return Result.success(safetyUser);
    }


    /**
     * 获取用户列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return 用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "获取用户列表")
    public Result<Page<SafetyUser>> listUser(
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestBody UserSearchDTO userSearchDTO) {
        Page<SafetyUser> safetyUserPage = userService.listUser(currentPage, pageSize, userSearchDTO);
        return Result.success(safetyUserPage);
    }


    @GetMapping("/list/{id}")
    @Operation(summary = "根据id获取用户信息")
    public Result<SafetyUser> getUserById(@PathVariable Long id) {
        SafetyUser safetyUser = userService.getUserById(id);
        return Result.success(safetyUser);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "根据id删除用户")
    public Result deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return Result.success();
    }
}
