package com.qapi.qapibackend.provider;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapibackend.model.entity.UserInterfaceInfo;

/**
 *
 */
public interface InnerUserInterfaceInfoService {
    /**
     * 调用接口统计
     *
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return 调用次数
     */
    boolean invokeCount(Long interfaceId, Long userId);

}
