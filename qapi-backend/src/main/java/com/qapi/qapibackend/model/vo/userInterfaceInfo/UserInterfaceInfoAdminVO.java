package com.qapi.qapibackend.model.vo.userInterfaceInfo;

import com.qapi.qapibackend.model.entity.InterfaceInfo;
import com.qapi.qapibackend.model.entity.User;
import com.qapi.qapibackend.model.entity.UserInterfaceInfo;
import com.qapi.qapibackend.model.vo.interfaceInfo.InterfaceInfoVO;
import com.qapi.qapibackend.model.vo.user.UserVO;
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
