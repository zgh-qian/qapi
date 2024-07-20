package com.qapi.qapibackend.model.dto.userInterfaceInfo;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer remainNum;

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
