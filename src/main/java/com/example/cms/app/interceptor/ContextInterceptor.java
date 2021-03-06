package com.example.cms.app.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class ContextInterceptor extends HandlerInterceptorAdapter {

    private final String API_BASE;

    public ContextInterceptor(String api_base) {
        API_BASE = api_base;
    }


    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        if(modelAndView != null){
            modelAndView.addObject("ADDED_API", API_BASE);

            final Optional<Object> avatarUrl = Optional.ofNullable(request.getSession().getAttribute("AVATAR_URL"));
            modelAndView.addObject("AVATAR_URL", avatarUrl.orElse(null));
        }
    }

}
