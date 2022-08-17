package com.jxiang.blog.controllers;

import com.jxiang.blog.services.AuthService;
import com.jxiang.blog.vo.params.LoginParams;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthService loginService;


    @PostMapping("login")
    public Result login(@RequestBody LoginParams loginParams) {
        return loginService.login(loginParams);
    }

}
