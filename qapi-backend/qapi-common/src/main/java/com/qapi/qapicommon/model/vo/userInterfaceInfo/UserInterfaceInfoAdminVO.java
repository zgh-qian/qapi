package com.qapi.qapicommon.model.vo.userInterfaceInfo;

import com.qapi.qapicommon.model.entity.InterfaceInfo;
import com.qapi.qapicommon.model.entity.User;
import com.qapi.qapicommon.model.entity.UserInterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserInterfaceInfoAdminVO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户
     */
    private User user;

    /**
     * 接口
     */
    private InterfaceInfo interfaceInfo;

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

    /**
     * object转vo
     *
     * @param userInterfaceInfo userInterfaceInfo
     * @return UserInterfaceInfoVO
     */
    public static UserInterfaceInfoAdminVO objToVO(UserInterfaceInfo userInterfaceInfo, User user, InterfaceInfo interfaceInfo) {
        if (userInterfaceInfo == null) {
            return null;
        }
        UserInterfaceInfoAdminVO userInterfaceInfoVO = new UserInterfaceInfoAdminVO();
        BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);
        userInterfaceInfoVO.setUser(user);
        userInterfaceInfoVO.setInterfaceInfo(interfaceInfo);
        return userInterfaceInfoVO;
    }
}
