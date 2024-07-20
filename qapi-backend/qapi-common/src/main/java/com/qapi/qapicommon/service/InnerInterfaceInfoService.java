package com.qapi.qapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapicommon.model.entity.InterfaceInfo;

public interface InnerInterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 根据接口路径和方法从数据库查询接口信息
     *
     * @param path   接口路径
     * @param method 方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
