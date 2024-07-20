package com.qapi.qapibackend.provider;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapibackend.model.entity.InterfaceInfo;

public interface InnerInterfaceInfoService {
    /**
     * 根据接口路径和方法从数据库查询接口信息
     *
     * @param url   接口路径
     * @param method 方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
