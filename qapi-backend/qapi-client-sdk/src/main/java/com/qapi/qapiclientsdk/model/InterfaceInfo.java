package com.qapi.qapiclientsdk.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfo implements Serializable {
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
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 模拟参数
     */
    private String mockParams;


    /**
     * 状态：0-未启用，1-启用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}