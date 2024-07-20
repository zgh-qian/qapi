package com.qapi.qapibackend.interceptors;

import com.qapi.qapibackend.common.BaseContext;
import com.qapi.qapibackend.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.qapi.qapibackend.constant.UserConstant.USER_LOGIN_STATE;

@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        //log.info("Global Interceptor... request url: {}", request.getRequestURI());
        // 如果请求的url是 /login ，则直接返回true
        if (request.getRequestURI().contains("/login")) {
            return true;
        }
        // 获取 session 中的用户信息
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser != null) {
            BaseContext.setUserId(currentUser.getId());
        }
        return true;
    }
}
