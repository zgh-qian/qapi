package com.qapi.qapibackend.controller;

import com.qapi.qapibackend.common.BaseResponse;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.ResultUtils;
import com.qapi.qapibackend.model.dto.user.UserLoginRequest;
import com.qapi.qapibackend.model.dto.user.UserRegisterRequest;
import com.qapi.qapibackend.model.dto.user.UserUpdateRequest;
import com.qapi.qapibackend.model.vo.user.LoginUserVO;
import com.qapi.qapibackend.model.vo.user.UserVO;
import com.qapi.qapibackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.qapi.qapibackend.constant.UserConstant.*;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
//@Api(tags = "用户相关接口")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 注册结果
     */
    @ApiOperation("注册")
    @PostMapping("/register")
    public BaseResponse<String> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        userService.userRegister(userRegisterRequest);
        return ResultUtils.success(USER_REGISTER_SUCCESS);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @return 登录结果
     */
    @ApiOperation("登录")
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        LoginUserVO loginUserVO = userService.userLogin(userLoginRequest, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @ApiOperation("登出")
    @PostMapping("/logout")
    public BaseResponse<String> userLogout(HttpServletRequest request) {
        userService.userLogout(request);
        return ResultUtils.success(USER_LOGOUT_SUCCESS);
    }

    /**
     * 用户注销
     *
     * @return 注销结果
     */
    @ApiOperation("注销")
    @PostMapping("/delete")
    public BaseResponse<String> userDelete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        userService.userDelete(deleteRequest, request);
        return ResultUtils.success(USER_DELETE_SUCCESS);
    }

    /**
     * 更新用户信息
     *
     * @return 更新结果
     */
    @ApiOperation("更新个人信息")
    @PostMapping("/update")
    public BaseResponse<String> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        userService.updateUser(userUpdateRequest, request);
        return ResultUtils.success(USER_UPDATE_SUCCESS);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 当前登录用户信息
     */
    @ApiOperation("查看登录用户信息")
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUserVO(HttpServletRequest request) {
        LoginUserVO loginUserVO = userService.getLoginUserVO(request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取脱敏用户信息
     *
     * @return 脱敏用户信息
     */
    @ApiOperation("获取脱敏用户信息")
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVo(Long id, HttpServletRequest request) {
        UserVO userVO = userService.getUserVo(id, request);
        return ResultUtils.success(userVO);
    }
}
