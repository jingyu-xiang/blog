package com.jxiang.blog.controller;

import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.util.beans.JwtUtils;
import com.jxiang.blog.vo.param.LoginParam;
import com.jxiang.blog.vo.param.RegisterParam;
import com.jxiang.blog.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private JwtUtils jwtUtils;

  @PostMapping("login")
  public Result login(@RequestBody LoginParam loginParam) {
    return authService.login(loginParam);
  }

  @PostMapping("logout")
  public Result logout(@RequestHeader("Authorization") String token) {
    return authService.logout(jwtUtils.removeHeader(token));
  }

  @PostMapping("register")
  public Result register(@RequestBody RegisterParam registerParam) {
    return authService.register(registerParam);
  }

}
