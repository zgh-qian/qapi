package com.qapi.qapibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapibackend.common.DeleteRequest;
import com.qapi.qapibackend.common.IdRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.qapi.qapibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.vo.interfaceInfo.InterfaceInfoVO;


public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 添加接口信息
     *
     * @param interfaceInfoAddRequest 接口信息
     */
    void addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest);

    /**
     * 删除接口信息
     *
     * @param deleteRequest 删除请求
     */
    void deleteInterfaceInfo(DeleteRequest deleteRequest);

    /**
     * 更新接口信息
     *
     * @param interfaceInfoUpdateRequest 接口信息
     */
    void updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest);

    /**
     * 根据id获取接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    InterfaceInfoVO getInterfaceInfoVo(Long id);

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest 接口信息查询请求
     * @return 接口信息分页
     */
    Page<InterfaceInfoVO> listInterfaceInfoVoPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 根据id获取接口信息
     *
     * @param id 接口id
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(Long id);

    /**
     * 分页查询接口信息
     *
     * @param interfaceInfoQueryRequest 接口信息查询请求
     * @return 接口信息分页
     */
    Page<InterfaceInfo> listInterfaceInfoPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 发布接口信息
     *
     * @param idRequest 发布请求
     */
    void onlineInterfaceInfo(IdRequest idRequest);

    /**
     * 下线接口信息
     *
     * @param idRequest 下线请求
     */
    void offlineInterfaceInfo(IdRequest idRequest);

    /**
     * 调用接口信息
     *
     * @param interfaceInfoInvokeRequest 接口调用请求
     * @return 接口调用结果
     */
    Object invokeInterfaceInfo(InterfaceInfoInvokeRequest interfaceInfoInvokeRequest);
}
