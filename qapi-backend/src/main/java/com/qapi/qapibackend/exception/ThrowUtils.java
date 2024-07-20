package com.qapi.qapibackend.exception;


import com.qapi.qapibackend.common.ErrorCode;

/**
 * 抛异常工具类
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition        条件
     * @param runtimeException 异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 异常
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 异常
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 异常
     * @param action    异常动作
     * @param message   异常信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String action, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
