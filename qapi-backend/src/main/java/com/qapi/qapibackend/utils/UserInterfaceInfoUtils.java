package com.qapi.qapibackend.utils;

import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.model.entity.UserInterfaceInfo;
import org.springframework.beans.BeanUtils;

public class UserInterfaceInfoUtils {
    public static UserInterfaceInfo validateInterfaceInfo(Object userInterfaceInfoObj, String action) {
        if (userInterfaceInfoObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoObj, userInterfaceInfo);
        return userInterfaceInfo;
    }
}
