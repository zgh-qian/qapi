package com.qapi.qapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapicommon.model.entity.User;


public interface InnerUserService extends IService<User> {

    /**
     * 根据accessKey和secretKey获取调用者信息
     *
     * @param accessKey 调用者accessKey
     * @param secretKey 调用者secretKey
     * @return 调用者信息
     */
    User getInvokeUser(String accessKey, String secretKey);
}
