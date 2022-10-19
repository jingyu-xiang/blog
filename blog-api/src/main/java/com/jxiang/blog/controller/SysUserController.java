package com.jxiang.blog.controller;

import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.util.beans.JwtUtils;
import com.jxiang.common.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/users")
public class SysUserController {

  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private JwtUtils jwtUtils;

  @GetMapping("me")
  public Result currentUser(@RequestHeader("Authorization") String token) {
    return sysUserService.findCurrentLoginUserVoByToken(
        jwtUtils.removeHeader(token));
  }

}
