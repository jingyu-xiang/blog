package com.jxiang.blog.handler;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.util.beans.JwtUtils;
import com.jxiang.blog.util.statics.SysUserThreadLocal;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

  final private AuthService authService;
  final private JwtUtils jwtUtils;

  @Autowired
  public AuthInterceptor(AuthService authService, JwtUtils jwtUtils) {
    this.authService = authService;
    this.jwtUtils = jwtUtils;
  }

  @Override
  public boolean preHandle(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler
  ) throws Exception {
    // before RestController methods, check if the request has a valid token
    if (!(handler instanceof HandlerMethod)) { // if handler is not RestController (e.g. ResourceController)
      return true;
    }

    String token = request.getHeader("Authorization");

    if (token == null) {
      Result result = Result.failure(ErrorCode.NO_LOGIN.getCode(),
          ErrorCode.NO_LOGIN.getMsg());
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().print(JSON.toJSONString(result));
      return false;
    }

    token = jwtUtils.removeHeader(token);

    log.info("============request start============");
    log.info("request URI: {}", request.getRequestURI());
    log.info("request method: {}", request.getMethod());
    log.info("token: {}", token);
    log.info("============request end============");

    SysUser sysUser = authService.checkToken(token);

    if (StringUtils.isBlank(token) || sysUser == null) {
      Result result = Result.failure(ErrorCode.NO_LOGIN.getCode(),
          ErrorCode.NO_LOGIN.getMsg());
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().print(JSON.toJSONString(result));
      return false;
    }

    // keep user info in thread local in the whole controller's execution scope
    SysUserThreadLocal.put(sysUser);

    return true;
  }

  @Override
  public void afterCompletion(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull Object handler, Exception ex
  ) throws Exception {
    // avoid memory leak after finish executing controller methods
    SysUserThreadLocal.remove();
  }

}
