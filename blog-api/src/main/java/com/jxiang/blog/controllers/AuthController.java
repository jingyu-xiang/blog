package com.jxiang.blog.controllers;

import com.jxiang.blog.services.AuthService;
import com.jxiang.blog.utils.JwtUtils;
import com.jxiang.blog.vo.params.LoginParams;
import com.jxiang.blog.vo.params.RegisterParams;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("login")
    public Result login(@RequestBody LoginParams loginParams) {
        return authService.login(loginParams);
    }

    @PostMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        return authService.logout(JwtUtils.removeHeader(token));
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterParams registerParams) {
        return authService.register(registerParams);
    }

}
