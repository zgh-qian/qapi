package com.qapi.qapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qapi.qapibackend.common.BaseContext;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.constant.CommonConstant;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.exception.ThrowUtils;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.entity.User;
import com.qapi.qapibackend.model.entity.UserInterfaceInfo;
import com.qapi.qapibackend.model.vo.interfaceInfo.InterfaceInfoVO;
import com.qapi.qapibackend.model.vo.user.UserVO;
import com.qapi.qapibackend.model.vo.userInterfaceInfo.UserInterfaceInfoAdminVO;
import com.qapi.qapibackend.model.vo.userInterfaceInfo.UserInterfaceInfoVO;
import com.qapi.qapibackend.service.InterfaceInfoService;
import com.qapi.qapibackend.service.UserInterfaceInfoService;
import com.qapi.qapibackend.mapper.UserInterfaceInfoMapper;
import com.qapi.qapibackend.service.UserService;
import com.qapi.qapibackend.utils.SqlUtils;
import com.qapi.qapibackend.utils.UserInterfaceInfoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.qapi.qapibackend.constant.UserInterfaceInfoConstant.*;

@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {
    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 添加用户接口记录
     *
     * @param userInterfaceInfoAddRequest 用户接口记录添加请求
     */
    @Override
    public void addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        UserInterfaceInfo userInterfaceInfo = UserInterfaceInfoUtils.validateInterfaceInfo(userInterfaceInfoAddRequest, USER_INTERFACE_INFO_ADD_FAILURE);
        Long userid = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        // 查询用户是否存在
        User user = userService.getById(userid);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_ADD_FAILURE, USER_INTERFACE_INFO_USER_NOT_EXIST);
        }
        // 查询接口信息是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_ADD_FAILURE, USER_INTERFACE_INFO_INTERFACE_INFO_NOT_EXIST);
        }
        // 保存用户接口记录
        boolean save = this.save(userInterfaceInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, USER_INTERFACE_INFO_ADD_FAILURE);
        }
    }

    /**
     * 删除用户接口记录
     *
     * @param deleteRequest 删除请求
     */
    @Override
    public void deleteUserInterfaceInfo(DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_DELETE_FAILURE);
        }
        Long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo userInterfaceInfo = this.getById(id);
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, USER_INTERFACE_INFO_DELETE_FAILURE, USER_INTERFACE_INFO_INTERFACE_INFO_NOT_EXIST);
        // 本人和管理员可以删除
        Long interfaceInfoUserId = userInterfaceInfo.getUserId();
        Long userId = BaseContext.getUserId();
        if (!(userId.equals(interfaceInfoUserId) || !userService.isAdmin())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, USER_INTERFACE_INFO_DELETE_FAILURE, USER_INTERFACE_INFO_INTERFACE_INFO_DELETE_PERMISSION_DENIED);
        }
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR, USER_INTERFACE_INFO_DELETE_FAILURE);
    }

    /**
     * 更新用户接口记录
     *
     * @param userInterfaceInfoUpdateRequest 用户接口记录更新请求
     */
    @Override
    public void updateUserInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        UserInterfaceInfo updateInterfaceInfo = UserInterfaceInfoUtils.validateInterfaceInfo(userInterfaceInfoUpdateRequest, USER_INTERFACE_INFO_UPDATE_FAILURE);
        UserInterfaceInfo userInterfaceInfo = this.getById(updateInterfaceInfo.getId());
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR, USER_INTERFACE_INFO_UPDATE_FAILURE, USER_INTERFACE_INFO_REQUEST_RECORD_NOT_EXIST);
        // 本人和管理员可以更新
        Long interfaceInfoUserId = userInterfaceInfo.getUserId();
        Long userId = BaseContext.getUserId();
        if (!(userId.equals(interfaceInfoUserId) || !userService.isAdmin())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, USER_INTERFACE_INFO_UPDATE_FAILURE, USER_INTERFACE_INFO_INTERFACE_INFO_UPDATE_PERMISSION_DENIED);
        }
        boolean update = this.updateById(updateInterfaceInfo);
        if (!update) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, USER_INTERFACE_INFO_UPDATE_FAILURE);
        }
    }

    /**
     * 获取用户接口记录详情
     *
     * @param id 用户接口记录id
     * @return 用户接口记录详情
     */
    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVo(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_SEARCH_FAILURE);
        }
        UserInterfaceInfo userInterfaceInfo = this.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, USER_INTERFACE_INFO_SEARCH_FAILURE, USER_INTERFACE_INFO_REQUEST_RECORD_NOT_EXIST);
        }
        UserVO userVO = UserVO.getUserVO(userService.getById(userInterfaceInfo.getUserId()));
        InterfaceInfoVO interfaceInfoVO = InterfaceInfoVO.objToVO(interfaceInfoService.getById(userInterfaceInfo.getInterfaceInfoId()));
        return UserInterfaceInfoVO.objToVO(userInterfaceInfo, userVO, interfaceInfoVO);
    }

    /**
     * 分页查询用户接口记录列表
     *
     * @param userInterfaceInfoQueryRequest 用户接口记录查询请求
     * @return 分页查询结果
     */
    @Override
    public Page<UserInterfaceInfoVO> listUserInterfaceInfoVoPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        Page<UserInterfaceInfo> userInterfaceInfoPage = listUserInterfaceInfoPage(userInterfaceInfoQueryRequest);
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = new Page<>();
        BeanUtils.copyProperties(userInterfaceInfoPage, userInterfaceInfoVOPage);
        List<UserInterfaceInfoVO> userInterfaceInfoVOList = new ArrayList<>();
        userInterfaceInfoPage.getRecords().stream().forEach(userInterfaceInfo -> {
            User user = userService.getById(userInterfaceInfo.getUserId());
            InterfaceInfo interfaceInfo = interfaceInfoService.getById(userInterfaceInfo.getInterfaceInfoId());
            userInterfaceInfoVOList.add(UserInterfaceInfoVO.objToVO(userInterfaceInfo, UserVO.getUserVO(user), InterfaceInfoVO.objToVO(interfaceInfo)));
        });
        userInterfaceInfoVOPage.setRecords(userInterfaceInfoVOList);
        return userInterfaceInfoVOPage;
    }

    /**
     * 根据id获取用户接口记录
     *
     * @param id 用户接口记录id
     * @return 用户接口记录
     */
    @Override
    public UserInterfaceInfoAdminVO getUserInterfaceInfo(Long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_SEARCH_FAILURE);
        }
        UserInterfaceInfo userInterfaceInfo = this.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, USER_INTERFACE_INFO_SEARCH_FAILURE, USER_INTERFACE_INFO_REQUEST_RECORD_NOT_EXIST);
        }
        User user = userService.getById(userInterfaceInfo.getUserId());
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(userInterfaceInfo.getInterfaceInfoId());
        return UserInterfaceInfoAdminVO.objToVO(userInterfaceInfo, user, interfaceInfo);
    }

    /**
     * 分页查询用户接口记录列表
     *
     * @param userInterfaceInfoQueryRequest 用户接口记录查询请求
     * @return 分页查询结果
     */
    @Override
    public Page<UserInterfaceInfoAdminVO> listUserInterfaceAdminVOInfoPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        Page<UserInterfaceInfo> userInterfaceInfoPage = listUserInterfaceInfoPage(userInterfaceInfoQueryRequest);
        Page<UserInterfaceInfoAdminVO> userInterfaceInfoAdminVOPage = new Page<>();
        BeanUtils.copyProperties(userInterfaceInfoPage, userInterfaceInfoAdminVOPage);
        List<UserInterfaceInfoAdminVO> userInterfaceInfoAdminVOList = new ArrayList<>();
        userInterfaceInfoPage.getRecords().stream().forEach(userInterfaceInfo -> {
            User user = userService.getById(userInterfaceInfo.getUserId());
            InterfaceInfo interfaceInfo = interfaceInfoService.getById(userInterfaceInfo.getInterfaceInfoId());
            userInterfaceInfoAdminVOList.add(UserInterfaceInfoAdminVO.objToVO(userInterfaceInfo, user, interfaceInfo));
        });
        userInterfaceInfoAdminVOPage.setRecords(userInterfaceInfoAdminVOList);
        return userInterfaceInfoAdminVOPage;
    }

    /**
     * 分页查询用户接口记录列表
     *
     * @param userInterfaceInfoQueryRequest 用户接口记录查询请求
     * @return 分页查询结果
     */
    @Override
    public Page<UserInterfaceInfo> listUserInterfaceInfoPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_SEARCH_FAILURE);
        }
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfoQueryRequest.getTotalNum();
        Integer remainNum = userInterfaceInfoQueryRequest.getRemainNum();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        int current = userInterfaceInfoQueryRequest.getCurrent();
        int pageSize = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (interfaceInfoId != null) {
            queryWrapper.eq("interface_info_id", interfaceInfoId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return this.page(new Page<>(current, pageSize), queryWrapper);
    }

    /**
     * 调用接口统计
     *
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return 调用次数
     */
    @Override
    public boolean invokeCount(Long interfaceId, Long userId) {
        if (interfaceId == null || userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_INVOKE_COUNT_FAILURE);
        }
        if (interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, USER_INTERFACE_INFO_INVOKE_COUNT_FAILURE);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        // remain_num > 1, remain_num - 1 , total_num + 1
        updateWrapper
                .eq("interface_info_id", interfaceId)
                .eq("user_id", userId)
                .ge("remain_num", 1)
                .setSql("remain_num = remain_num - 1, total_num = total_num + 1");
        return this.update(updateWrapper);
    }
}




