package com.qapi.qapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qapi.qapibackend.common.BaseContext;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.exception.ThrowUtils;
import com.qapi.qapibackend.model.dto.user.UserLoginRequest;
import com.qapi.qapibackend.model.dto.user.UserRegisterRequest;
import com.qapi.qapibackend.model.dto.user.UserUpdateRequest;
import com.qapi.qapibackend.model.entity.User;
import com.qapi.qapibackend.model.vo.user.LoginUserVO;
import com.qapi.qapibackend.model.vo.user.UserVO;
import com.qapi.qapibackend.service.UserService;
import com.qapi.qapibackend.mapper.UserMapper;
import com.qapi.qapibackend.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.qapi.qapibackend.constant.UserConstant.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     */
    @Override
    public void userRegister(UserRegisterRequest userRegisterRequest) {
        // 参数校验
        User user = UserUtils.validateUser(userRegisterRequest, USER_REGISTER_FAILED);
        String username = user.getUsername();
        synchronized (username.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_USERNAME_EXIST);
            }
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, USER_DATABASE_ERROR);
            }
        }
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request          HTTP请求
     * @return 登录用户信息
     */
    @Override
    public LoginUserVO userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 参数校验
        User user = UserUtils.validateUser(userLoginRequest, USER_LOGIN_FAILED);
        String username = user.getUsername();
        String password = user.getPassword();
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User loginUser = this.baseMapper.selectOne(queryWrapper);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_LOGIN_FAILED, USER_USERNAME_NOT_EXIST);
        }
        // 校验密码
        if (!loginUser.getPassword().equals(password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_LOGIN_FAILED, USER_PASSWORD_ERROR);
        }
        // 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, loginUser);
        // 返回登录用户信息
        return LoginUserVO.getLoginUserVO(loginUser);
    }

    /**
     * 用户登出
     *
     * @param request HTTP请求
     */
    @Override
    public void userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_LOGOUT_FAILED, USER_NOT_LOGIN);
        }
        // 清除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    /**
     * 用户注销
     *
     * @param deleteRequest 注销请求
     * @param request       HTTP请求
     */
    @Override
    public void userDelete(DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_DELETE_FAILED, USER_PARAM_NULL);
        }
        boolean removeById = this.removeById(deleteRequest.getId());
        if (!removeById) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, USER_DATABASE_ERROR);
        }
        // 清除用户登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 用户更新请求
     * @param request           HTTP请求
     */
    @Override
    public void updateUser(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 校验参数
        User user = UserUtils.validateUser(userUpdateRequest, USER_UPDATE_FAILED);
        user.setId(BaseContext.getUserId());
        boolean updateById = this.updateById(user);
        ThrowUtils.throwIf(!updateById, ErrorCode.OPERATION_ERROR, USER_UPDATE_FAILED, USER_DATABASE_ERROR);
        // 更新用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
    }

    /**
     * 获取用户信息
     *
     * @param request HTTP请求
     * @return 用户信息
     */
    @Override
    public UserVO getUserVO(HttpServletRequest request) {
        // 判断是否已登录
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_NOT_LOGIN);
        }
        // 查询用户信息
        User loginUser = this.getById(user.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, USER_USER_NOT_EXIST);
        }
        return UserVO.getUserVO(loginUser);
    }

    /**
     * 获取登录用户信息
     *
     * @param request HTTP请求
     * @return 登录用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(HttpServletRequest request) {
        // 判断是否已登录
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_NOT_LOGIN);
        }
        // 查询用户信息
        User loginUser = this.getById(user.getId());
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, USER_USER_NOT_EXIST);
        }
        // 返回登录用户信息
        return LoginUserVO.getLoginUserVO(loginUser);
    }

    /**
     * 获取脱敏用户信息
     *
     * @param id      用户id
     * @param request HTTP请求
     * @return 脱敏用户信息
     */
    @Override
    public UserVO getUserVo(Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, USER_USER_NOT_EXIST);
        }
        return UserVO.getUserVO(user);
    }

    /**
     * 判断是否为管理员
     *
     * @return 是否为管理员
     */
    @Override
    public Boolean isAdmin() {
        Long userId = BaseContext.getUserId();
        if (userId == null) {
            return false;
        }
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }
        if (ADMIN_ROLE.equals(user.getRole())) {
            return true;
        }
        return false;
    }
}




