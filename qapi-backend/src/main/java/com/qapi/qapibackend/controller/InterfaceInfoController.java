package com.qapi.qapibackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qapi.qapibackend.annotation.AuthCheck;
import com.qapi.qapibackend.common.BaseResponse;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.IdRequest;
import com.qapi.qapibackend.common.ResultUtils;
import com.qapi.qapibackend.constant.UserConstant;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.vo.interfaceInfo.InterfaceInfoVO;
import com.qapi.qapibackend.service.InterfaceInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.qapi.qapibackend.constant.InterfaceInfoConstant.*;

/**
 * 接口管理相关接口
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
//@Api(tags = "接口管理相关接口")
public class InterfaceInfoController {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest 接口信息
     * @return INTERFACE_INFO_CREATE_SUCCESS
     */
    @ApiOperation("添加接口")
    @PostMapping("/add")
    public BaseResponse<String> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest);
        return ResultUtils.success(INTERFACE_INFO_CREATE_SUCCESS);
    }

    /**
     * 删除接口
     *
     * @param deleteRequest 删除请求
     * @return INTERFACE_INFO_DELETE_SUCCESS
     */
    @ApiOperation("删除接口")
    @PostMapping("/delete")
    public BaseResponse<String> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        interfaceInfoService.deleteInterfaceInfo(deleteRequest);
        return ResultUtils.success(INTERFACE_INFO_DELETE_SUCCESS);
    }

    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest 接口信息
     * @return INTERFACE_INFO_UPDATE_SUCCESS
     */
    @ApiOperation("更新接口")
    @PostMapping("/update")
    public BaseResponse<String> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest);
        return ResultUtils.success(INTERFACE_INFO_UPDATE_SUCCESS);
    }

    /**
     * 根据id获取脱敏接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    @ApiOperation("查询脱敏接口")
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfoVO> getInterfaceInfoVo(Long id) {
        InterfaceInfoVO interfaceInfoVO = interfaceInfoService.getInterfaceInfoVo(id);
        return ResultUtils.success(interfaceInfoVO);
    }

    /**
     * 分页查询脱敏接口信息
     *
     * @param interfaceInfoQueryRequest 接口信息
     * @return 分页接口信息
     */
    @ApiOperation("分页查询脱敏接口")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<InterfaceInfoVO>> listInterfaceInfoVoPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfoVO> interfaceInfoVOPage = interfaceInfoService.listInterfaceInfoVoPage(interfaceInfoQueryRequest);
        return ResultUtils.success(interfaceInfoVOPage);
    }

    /**
     * 根据id获取接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    @ApiOperation("查询接口")
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<InterfaceInfo> getInterfaceInfo(Long id) {
        InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfo(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest 接口信息
     * @return 分页接口信息
     */
    @ApiOperation("分页查询接口")
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.listInterfaceInfoPage(interfaceInfoQueryRequest);
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 发布接口
     *
     * @param idRequest 接口id
     * @return INTERFACE_INFO_ONLINE_SUCCESS
     */
    @ApiOperation("发布接口")
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        interfaceInfoService.onlineInterfaceInfo(idRequest);
        return ResultUtils.success(INTERFACE_INFO_ONLINE_SUCCESS);
    }

    /**
     * 下线接口
     *
     * @param idRequest 接口id
     * @return INTERFACE_INFO_OFFLINE_SUCCESS
     */
    @ApiOperation("下线接口")
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        interfaceInfoService.offlineInterfaceInfo(idRequest);
        return ResultUtils.success(INTERFACE_INFO_OFFLINE_SUCCESS);
    }

    /**
     * 调用接口
     *
     * @param interfaceInfoInvokeRequest 接口调用请求
     * @return 接口调用结果
     */
    @ApiOperation("调用接口")
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest) {
        Object result = interfaceInfoService.invokeInterfaceInfo(interfaceInfoInvokeRequest);
        return ResultUtils.success(result);
    }
}
