package com.jxiang.blog.services;

import com.jxiang.blog.pojo.SysUser;
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

}
