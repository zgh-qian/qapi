package com.qapi.qapibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qapi.qapibackend.annotation.AuthCheck;
import com.qapi.qapibackend.common.BaseResponse;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.ResultUtils;
import com.qapi.qapibackend.constant.UserConstant;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.qapi.qapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.qapi.qapibackend.model.entity.UserInterfaceInfo;
import com.qapi.qapibackend.model.vo.userInterfaceInfo.UserInterfaceInfoAdminVO;
import com.qapi.qapibackend.model.vo.userInterfaceInfo.UserInterfaceInfoVO;
import com.qapi.qapibackend.service.UserInterfaceInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.qapi.qapibackend.constant.UserInterfaceInfoConstant.*;

/**
 * 接口管理相关接口
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
//@Api(tags = "用户接口管理相关接口")
public class UserInterfaceInfoController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 添加用户接口记录
     *
     * @param userInterfaceInfoAddRequest 用户接口记录添加请求
     * @return USER_INTERFACE_INFO_ADD_SUCCESS
     */
    @ApiOperation("添加用户接口记录")
    @PostMapping("/add")
    public BaseResponse<String> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        userInterfaceInfoService.addUserInterfaceInfo(userInterfaceInfoAddRequest);
        return ResultUtils.success(USER_INTERFACE_INFO_ADD_SUCCESS);
    }

    /**
     * 删除用户接口记录
     *
     * @param deleteRequest 删除请求
     * @return USER_INTERFACE_INFO_DELETE_SUCCESS
     */
    @ApiOperation("删除用户接口记录")
    @PostMapping("/delete")
    public BaseResponse<String> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        userInterfaceInfoService.deleteUserInterfaceInfo(deleteRequest);
        return ResultUtils.success(USER_INTERFACE_INFO_DELETE_SUCCESS);
    }

    /**
     * 更新用户接口记录
     *
     * @param userInterfaceInfoUpdateRequest 用户接口记录更新请求
     * @return USER_INTERFACE_INFO_UPDATE_SUCCESS
     */
    @ApiOperation("更新用户接口记录")
    @PostMapping("/update")
    public BaseResponse<String> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        userInterfaceInfoService.updateUserInterfaceInfo(userInterfaceInfoUpdateRequest);
        return ResultUtils.success(USER_INTERFACE_INFO_UPDATE_SUCCESS);
    }

    /**
     * 查询脱敏用户接口记录
     *
     * @param id 用户接口记录id
     * @return 用户接口记录vo
     */
    @ApiOperation("查询脱敏用户接口记录")
    @GetMapping("/get/vo")
    public BaseResponse<UserInterfaceInfoVO> getUserInterfaceInfoVo(Long id) {
        UserInterfaceInfoVO userInterfaceInfoVO = userInterfaceInfoService.getUserInterfaceInfoVo(id);
        return ResultUtils.success(userInterfaceInfoVO);
    }

    /**
     * 分页查询脱敏用户接口记录
     *
     * @param userInterfaceInfoQueryRequest 接口查询请求
     * @return 脱敏用户接口记录
     */
    @ApiOperation("分页查询脱敏用户接口记录")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserInterfaceInfoVO>> listUserInterfaceInfoVoPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        Page<UserInterfaceInfoVO> userInterfaceInfoVOPage = userInterfaceInfoService.listUserInterfaceInfoVoPage(userInterfaceInfoQueryRequest);
        return ResultUtils.success(userInterfaceInfoVOPage);
    }

    /**
     * 查询用户接口记录
     *
     * @param id 用户接口记录id
     * @return 用户接口记录
     */
    @ApiOperation("查询用户接口记录")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfoAdminVO> getUserInterfaceInfo(Long id) {
        UserInterfaceInfoAdminVO userInterfaceInfoAdminVO = userInterfaceInfoService.getUserInterfaceInfo(id);
        return ResultUtils.success(userInterfaceInfoAdminVO);
    }

    /**
     * 分页查询用户接口记录
     *
     * @param userInterfaceInfoQueryRequest 接口查询请求
     * @return 用户接口记录
     */
    @ApiOperation("分页查询用户接口记录")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfoAdminVO>> listUserInterfaceAdminVOInfoPage(@RequestBody UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        Page<UserInterfaceInfoAdminVO> listUserInterfaceInfoAdminVOPage = userInterfaceInfoService.listUserInterfaceAdminVOInfoPage(userInterfaceInfoQueryRequest);
        return ResultUtils.success(listUserInterfaceInfoAdminVOPage);
    }
}
