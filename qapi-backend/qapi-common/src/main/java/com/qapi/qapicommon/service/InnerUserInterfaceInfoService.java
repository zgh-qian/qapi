package com.qapi.qapicommon.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qapi.qapicommon.model.entity.UserInterfaceInfo;

/**
 *
 */
public interface InnerUserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 调用接口统计
     *
     * @param interfaceId 接口id
     * @param userId      用户id
     * @return 调用次数
     */
    boolean invokeCount(Long interfaceId, Long userId);

}
