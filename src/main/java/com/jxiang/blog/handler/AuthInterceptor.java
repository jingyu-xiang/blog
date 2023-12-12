package com.jxiang.blog.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.util.JwtUtils;
import com.jxiang.blog.util.SysUserThreadLocal;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  final private AuthService authService;

  final private JwtUtils jwtUtils;

  @Override
  public boolean preHandle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler) throws Exception {
    // before RestController methods, check if the request has a valid token
    // if handler is not RestController (e.g. ResourceController)
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }

    String token = request.getHeader("Authorization");

    if (token == null) {
      final Result result = Result.failure(
          ErrorCode.NO_LOGIN.getCode(),
          ErrorCode.NO_LOGIN.getMsg());
      response.setContentType("application/json;charset=utf-8");
      response.getWriter().print(JSON.toJSONString(result));
      return false;
    }

    token = jwtUtils.removeHeader(token);

    AuthInterceptor.log.info("============request start============");
    AuthInterceptor.log.info("request URI: {}", request.getRequestURI());
    AuthInterceptor.log.info("request method: {}", request.getMethod());
    AuthInterceptor.log.info("token: {}", token);
    AuthInterceptor.log.info("============request end============");

    final SysUser sysUser = authService.checkToken(token);

    if (StringUtils.isBlank(token) || sysUser == null) {
      final Result result = Result.failure(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
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
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Object handler, final Exception ex) {
    // avoid memory leak after finish executing controller methods
    SysUserThreadLocal.remove();
  }

}
