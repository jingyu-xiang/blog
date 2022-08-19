package com.jxiang.blog.controllers;

import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.utils.JwtUtils;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("me")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(jwtUtils.removeHeader(token));
    }

}
