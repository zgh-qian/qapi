package com.qapi.qapibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.qapi.qapibackend.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapibackend.model.vo.userInterfaceInfo.UserInterfaceInfoAdminVO;
import com.qapi.qapibackend.model.vo.userInterfaceInfo.UserInterfaceInfoVO;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 添加用户接口记录
     *
     * @param userInterfaceInfoAddRequest 用户接口记录添加请求
     */
    void addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest);

    /**
     * 删除用户接口记录
     *
     * @param deleteRequest 删除请求
     */
    void deleteUserInterfaceInfo(DeleteRequest deleteRequest);

    /**
     * 更新用户接口记录
     *
     * @param userInterfaceInfoUpdateRequest 用户接口记录更新请求
     */
    void updateUserInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest);

    /**
     * 获取脱敏用户接口记录详情
     *
     * @param id 用户接口记录id
     * @return 脱敏用户接口记录详情
     */
    UserInterfaceInfoVO getUserInterfaceInfoVo(Long id);

    /**
     * 分页查询脱敏用户接口记录
     *
     * @param userInterfaceInfoQueryRequest 用户接口记录查询请求
     * @return 分页查询结果
     */
    Page<UserInterfaceInfoVO> listUserInterfaceInfoVoPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 根据id获取用户接口记录
     *
     * @param id 用户接口记录id
     * @return 用户接口记录
     */
    UserInterfaceInfoAdminVO getUserInterfaceInfo(Long id);

    /**
     * 分页查询用户接口记录
     *
     * @param userInterfaceInfoQueryRequest 用户接口记录查询请求
     * @return 分页查询结果
     */
    Page<UserInterfaceInfoAdminVO> listUserInterfaceAdminVOInfoPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 分页查询用户接口记录
     *
     * @param userInterfaceInfoQueryRequest 用户接口记录查询请求
     * @return 分页查询结果
     */
    Page<UserInterfaceInfo> listUserInterfaceInfoPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 调用接口统计
     *
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return 调用次数
     */
    boolean invokeCount(Long interfaceId, Long userId);

}
