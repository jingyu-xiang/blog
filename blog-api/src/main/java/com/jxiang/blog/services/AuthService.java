package com.jxiang.blog.services;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.vo.params.LoginParams;
import com.jxiang.blog.vo.results.Result;

public interface AuthService {

    /**
     * Login feature
     * take an object of account and password, return token and store it in redis {user: token}
     * test passwords: {password}
     *
     * @param loginParams account & password
     * @return Result
     */
    Result login(LoginParams loginParams);

    /**
     * Check if a jwt token is valid or not
     *
     * @param token jwt token
     * @return SysUser
     */
    SysUser checkToken(String token);

}
