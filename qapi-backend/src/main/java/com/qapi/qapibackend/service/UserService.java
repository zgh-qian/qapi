package com.qapi.qapibackend.service;

import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.model.dto.user.UserLoginRequest;
import com.qapi.qapibackend.model.dto.user.UserRegisterRequest;
import com.qapi.qapibackend.model.dto.user.UserUpdateRequest;
import com.qapi.qapibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapibackend.model.vo.user.LoginUserVO;
import com.qapi.qapibackend.model.vo.user.UserVO;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     */
    void userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request          HTTP请求
     * @return 登录用户信息
     */
    LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 用户登出
     *
     * @param request HTTP请求
     */
    void userLogout(HttpServletRequest request);

    /**
     * 用户注销
     *
     * @param deleteRequest 注销请求
     * @param request       HTTP请求
     */
    void userDelete(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 用户更新请求
     * @param request           HTTP请求
     */
    void updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 获取用户信息
     *
     * @param request HTTP请求
     * @return 登录用户信息
     */
    UserVO getUserVO(HttpServletRequest request);

    /**
     * 获取登录用户信息
     *
     * @param request HTTP请求
     * @return 登录用户信息
     */
    LoginUserVO getLoginUserVO(HttpServletRequest request);

    /**
     * 获取脱敏用户信息
     *
     * @param id      用户id
     * @param request HTTP请求
     * @return 用户信息
     */
    UserVO getUserVo(Long id, HttpServletRequest request);

    /**
     * 判断是否是管理员
     *
     * @return 是否是管理员
     */
    Boolean isAdmin();
}
