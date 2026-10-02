package com.notzed.rahmaApp.event_service.auth;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String userId = request.getHeader("X-User-Id");
        if(userId != null){
            UserContextHolder.setCurrentUserId(Long.valueOf(userId));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse respose, Object handler, @Nullable Exception ex) throws Exception{
        UserContextHolder.clear();
    }

}
