package com.jxiang.blog.configs;

import com.jxiang.blog.services.LoginService;
import com.jxiang.blog.vo.params.LoginParams;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping
    public Result login(LoginParams loginParams) {
        return loginService.login(loginParams);
    }

}
