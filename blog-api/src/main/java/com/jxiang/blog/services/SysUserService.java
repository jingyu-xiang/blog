package com.jxiang.blog.services;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.vo.results.Result;
import org.springframework.stereotype.Service;

@Service
public interface SysUserService {

    /**
     * retrieve a user by a given id
     *
     * @param id sysUser's id
     * @return single sysUser
     */
    SysUser findUserById(Long id);

    /**
     * retrieve a user based on its username and password
     *
     * @param account  user account
     * @param password user password
     * @return
     */
    SysUser findAuthUser(String account, String password);

    /**
     * retrieve user's authUserVo according to token
     *
     * @param token jwt token
     * @return Result
     */
    Result findUserByToken(String token);

}
