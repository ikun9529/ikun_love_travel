package com.ikun.config.interceptor;

import com.ikun.common.constant.Code;
import com.ikun.exception.BussinessException;
import com.ikun.model.domain.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.getBean().getClass().getName().equals("springfox.documentation.swagger.web.ApiResourceController")) {
            return true;
        }
        // 1.判断是否需要拦截（ThreadLocal中是否有用户）
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截
            throw new BussinessException(Code.USER_NOT_LOGIN, "用户未登录");
        }
        // 有用户，则放行
        return true;
    }
}
