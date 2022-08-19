package com.jxiang.blog.handlers;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.services.AuthService;
import com.jxiang.blog.utils.JwtUtils;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // before RestController methods, check if the request has a valid token
        if (!(handler instanceof HandlerMethod)) { // if handler is RestController (e.g. ResourceController), pass
            return true;
        }

        String token = jwtUtils.removeHeader(request.getHeader("Authorization"));
        System.out.println(token);

        log.info("============request start============");
        log.info("request URI: {}", request.getRequestURI());
        log.info("request method: {}", request.getMethod());
        log.info("token: {}", token);
        log.info("============request end============");

        if (StringUtils.isBlank(token) || authService.checkToken(token) == null) {
            Result result = Result.failure(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        return true;
    }

}
