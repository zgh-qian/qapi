package com.qapi.qapibackend.model.dto.interfaceInfo;


import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户传递的请求参数
     */
    private String userRequestParams;


    private static final long serialVersionUID = 1L;
}
