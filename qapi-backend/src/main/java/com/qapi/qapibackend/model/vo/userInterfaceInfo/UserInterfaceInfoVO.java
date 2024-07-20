package com.qapi.qapibackend.model.vo.userInterfaceInfo;

import com.qapi.qapibackend.model.entity.UserInterfaceInfo;
import com.qapi.qapibackend.model.vo.interfaceInfo.InterfaceInfoVO;
import com.qapi.qapibackend.model.vo.user.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class UserInterfaceInfoVO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户
     */
    private UserVO userVO;

    /**
     * 接口
     */
    private InterfaceInfoVO interfaceInfoVO;

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
    public static UserInterfaceInfoVO objToVO(UserInterfaceInfo userInterfaceInfo, UserVO userVO, InterfaceInfoVO interfaceInfoVO) {
        if (userInterfaceInfo == null) {
            return null;
        }
        UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);
        userInterfaceInfoVO.setUserVO(userVO);
        userInterfaceInfoVO.setInterfaceInfoVO(interfaceInfoVO);
        return userInterfaceInfoVO;
    }
}
