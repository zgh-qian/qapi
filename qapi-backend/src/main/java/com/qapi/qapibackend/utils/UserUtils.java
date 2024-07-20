package com.qapi.qapibackend.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.qapi.qapibackend.common.ErrorCode;
import com.qapi.qapibackend.exception.BusinessException;
import com.qapi.qapibackend.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.qapi.qapibackend.constant.UserConstant.*;

public class UserUtils {
    /**
     * 加密密码
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String getEncryptedPassword(String password) {
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    /**
     * 生成 Key
     *
     * @param signature 签名
     * @param length    长度
     * @return Key
     */
    public static String getKey(String signature, Integer length) {
        return DigestUtil.md5Hex(SALT + signature + RandomUtil.randomNumbers(length));
    }

    /**
     * 验证邮箱是否合法
     *
     * @param email 邮箱
     * @return true 合法 false 不合法
     */
    public static Boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // 邮箱正则表达式
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 验证用户信息
     *
     * @param userObj 用户对象
     * @param action  操作
     * @return 用户对象
     */
    public static User validateUser(Object userObj, String action) {
        if (userObj == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_PARAM_NULL);
        }
        User user = new User();
        BeanUtils.copyProperties(userObj, user);
        Long id = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String nickname = user.getNickname();
        String avatar = user.getAvatar();
        Integer gender = user.getGender();
        Date birthday = user.getBirthday();
        String phone = user.getPhone();
        String address = user.getAddress();
        // 验证用户信息
        if (id != null && id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_ID_ERROR);
        }
        // 如果是登录注册，则验证用户名、密码不能为空
        if (action.equals(USER_REGISTER_FAILED) || action.equals(USER_LOGIN_FAILED)) {
            if (StrUtil.isBlank(username)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_USERNAME_NULL);
            }
            if (StrUtil.isBlank(password)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_PASSWORD_NULL);
            }
        }
        // 用户名
        if (username != null && (username.length() < 4 || username.length() > 20)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_USERNAME_LENGTH);
        }
        // 密码
        if (password != null && (password.length() < 6 || password.length() > 20)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_PASSWORD_LENGTH);
        }
        // 邮箱
        if (email != null && !validateEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_EMAIL_ERROR);
        }
        // 昵称
        if (nickname != null && nickname.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_NICKNAME_LENGTH);
        }
        // 头像
        if (avatar != null && avatar.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_AVATAR_ERROR);
        }
        // 性别
        if (gender != null && (gender != 0 && gender != 1 && gender != 2)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_GENDER_ERROR);
        }
        // 生日
        if (birthday != null && birthday.after(new Date())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_BIRTHDAY_ERROR);
        }
        // 手机号
        if (phone != null && phone.length() != 11) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_PHONE_LENGTH);
        }
        // 地址
        if (address != null && address.length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, action, USER_PHONE_LENGTH);
        }
        // 加密密码
        if (password != null) {
            user.setPassword(getEncryptedPassword(password));
        }
        // 如果是注册，则生成 accessKey 和 secretKey
        if (action.equals(USER_REGISTER_FAILED)) {
            user.setAccessKey(getKey(username, 5));
            user.setSecretKey(getKey(username, 8));
        }
        return user;
    }
}
