package com.qapi.qapibackend.model.dto.interfaceInfo;


import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 模拟参数
     */
    private String mockParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 描述
     */
    private String description;

    /**
     * 调用次数
     */
    private Integer callCount;

    /**
     * 状态：0-未启用，1-启用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
