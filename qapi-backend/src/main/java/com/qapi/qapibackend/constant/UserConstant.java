package com.qapi.qapibackend.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "gapi_user_login_state";

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 密码盐
     */
    String SALT = "qapi";

    String USER_REGISTER_SUCCESS = "注册成功";

    String USER_REGISTER_FAILED = "注册失败";

    String USER_LOGIN_FAILED = "登录失败";

    String USER_LOGOUT_SUCCESS = "登出成功";

    String USER_LOGOUT_FAILED = "登出失败";

    String USER_DELETE_SUCCESS = "删除成功";

    String USER_DELETE_FAILED = "删除失败";

    String USER_UPDATE_SUCCESS = "更新成功";

    String USER_UPDATE_FAILED = "更新失败";

    String USER_ID_ERROR = "用户ID错误";

    String USER_PARAM_NULL = "参数不能为空";

    String USER_USERNAME_NULL = "用户名不能为空";

    String USER_PASSWORD_NULL = "密码不能为空";

    String USER_EMAIL_ERROR = "邮箱格式错误";

    String USER_USERNAME_LENGTH = "用户名长度必须在 4-20 之间";

    String USER_PASSWORD_LENGTH = "密码长度必须在 6-20 之间";

    String USER_NICKNAME_LENGTH = "昵称长度必须在 1-20 之间";

    String USER_USERNAME_EXIST = "账号不能重复";

    String USER_DATABASE_ERROR = "数据库错误";

    String USER_USER_NOT_EXIST = "用户不存在";
    String USER_USERNAME_NOT_EXIST = "账号不存在";

    String USER_PASSWORD_ERROR = "密码错误";

    String USER_NOT_LOGIN = "未登录";

    String USER_AVATAR_ERROR = "头像格式错误";

    String USER_PHONE_LENGTH = "手机号长度必须是 11 位数";

    String USER_GENDER_ERROR = "";

    String USER_BIRTHDAY_ERROR = "生日格式错误";

}
