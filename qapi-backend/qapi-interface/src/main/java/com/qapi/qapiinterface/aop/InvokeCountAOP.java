package com.qapi.qapiinterface.aop;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * aop切面类，用于统计接口调用次数
 * 如果用这个的话，只能适用于单个项目，如果有多个项目，需要在每个项目中都添加这个切面类，比较麻烦
 * 所以推荐使用网关来统一管理接口调用次数
 */
@RestControllerAdvice
public class InvokeCountAOP {

}
