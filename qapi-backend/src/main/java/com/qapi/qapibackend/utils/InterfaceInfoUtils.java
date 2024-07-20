package com.qapi.qapibackend.utils;

import cn.hutool.core.util.StrUtil;
import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.model.entity.InterfaceInfo;
import org.springframework.beans.BeanUtils;


import static com.qapi.qapibackend.constant.InterfaceInfoConstant.*;

public class InterfaceInfoUtils {
    public static InterfaceInfo validateInterfaceInfo(Object interfaceInfoObj, String action) {
        if (interfaceInfoObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, INTERFACE_INFO_PARAM_NULL);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoObj, interfaceInfo);
        Long id = interfaceInfo.getId();
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String description = interfaceInfo.getDescription();
        // 如果是创建或者更新接口信息，则需要校验参数是否为空
        if (action.equals(INTERFACE_INFO_CREATE_FAILURE) || action.equals(INTERFACE_INFO_UPDATE_FAILURE)) {
            if (action.equals(INTERFACE_INFO_UPDATE_FAILURE) && id == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_ID_NOT_NULL);
            }
            if (StrUtil.isBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_NAME_NOT_NULL);
            }
            if (StrUtil.isBlank(url)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_URL_NOT_NULL);
            }
            if (StrUtil.isBlank(method)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_METHOD_NOT_NULL);
            }
        }
        if (name != null && name.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_NAME_MAX_LENGTH);
        }
        if (url != null && url.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_URL_MAX_LENGTH);
        }
        if (method != null && method.length() > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_METHOD_MAX_LENGTH);
        }
        if (requestHeader != null && requestHeader.length() > 500) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_REQUEST_HEADER_MAX_LENGTH);
        }
        if (responseHeader != null && responseHeader.length() > 500) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_RESPONSE_HEADER_MAX_LENGTH);
        }
        if (description != null && description.length() > 500) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, INTERFACE_INFO_DESC_MAX_LENGTH);
        }
        return interfaceInfo;
    }
}
