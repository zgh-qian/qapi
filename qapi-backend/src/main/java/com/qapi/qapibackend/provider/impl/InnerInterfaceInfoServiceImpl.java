package com.qapi.qapibackend.provider.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qapi.qapibackend.provider.InnerInterfaceInfoService;
import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.mapper.InterfaceInfoMapper;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url).eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
