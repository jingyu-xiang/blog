package com.jxiang.blog.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.util.JwtUtils;
import com.jxiang.blog.vo.param.LoginParam;
import com.jxiang.blog.vo.param.RegisterParam;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("login")
  public Result login(@RequestBody final LoginParam loginParam) {
    return authService.login(loginParam);
  }

  @PostMapping("logout")
  public Result logout(@RequestHeader("Authorization") final String token) {
    return authService.logout(JwtUtils.removeHeader(token));
  }

  @PostMapping("register")
  public Result register(@RequestBody final RegisterParam registerParam) {
    return authService.register(registerParam);
  }

}
