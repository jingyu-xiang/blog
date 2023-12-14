package com.jxiang.blog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.util.JwtUtils;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class SysUserController {

  private final SysUserService sysUserService;

  @GetMapping("me")
  public Result currentUser(@RequestHeader("Authorization") final String token) {
    return sysUserService.findCurrentLoginUserVoByToken(JwtUtils.removeHeader(token));
  }

}
