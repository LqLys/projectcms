package com.example.cms.security.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextInterceptor extends HandlerInterceptorAdapter {

    private final String API_BASE;

    public ContextInterceptor(String api_base) {
        API_BASE = api_base;
    }


    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
        if(modelAndView != null){
            modelAndView.addObject("ADDED_API", API_BASE);
        }
        if(request.getRequestURI().equals("/login")){

        }
    }

}
