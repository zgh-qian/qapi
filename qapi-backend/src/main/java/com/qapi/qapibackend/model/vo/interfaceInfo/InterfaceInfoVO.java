package com.qapi.qapibackend.model.vo.interfaceInfo;

import com.qapi.qapibackend.model.entity.InterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class InterfaceInfoVO {
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

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 对象转包装类
     *
     * @param interfaceInfo 接口信息实体类
     * @return 接口信息包装类
     */
    public static InterfaceInfoVO objToVO(InterfaceInfo interfaceInfo) {
        if (interfaceInfo == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
        return interfaceInfoVO;
    }
}
