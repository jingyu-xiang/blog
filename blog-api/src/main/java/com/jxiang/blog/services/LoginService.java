package com.jxiang.blog.services;

import com.jxiang.blog.vo.params.LoginParams;
import com.jxiang.blog.vo.results.Result;

public interface LoginService {

    /**
     * Login feature
     * take an object of account and password, return token and store it in redis {user: token}
     *
     * @param loginParams account & password
     * @return Result
     */
    Result login(LoginParams loginParams);

}
