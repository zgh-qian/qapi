package com.qapi.qapibackend.provider;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapibackend.model.entity.User;


public interface InnerUserService {

    /**
     * 根据accessKey和secretKey获取调用者信息
     *
     * @param accessKey 调用者accessKey
     * @return 调用者信息
     */
    User getInvokeUser(String accessKey);
}
