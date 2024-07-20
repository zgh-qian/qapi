package com.qapi.qapibackend.provider.impl;

import com.qapi.qapibackend.provider.InnerUserInterfaceInfoService;
import com.qapi.qapibackend.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(Long interfaceId, Long userId) {
        return userInterfaceInfoService.invokeCount(interfaceId, userId);
    }
}
