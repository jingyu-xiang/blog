package com.jxiang.blog.services.impl;

import com.jxiang.blog.dao.SysUserMapper;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.services.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public SysUser findUserById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            user = new SysUser();
            user.setNickname("unknown");
        }
        return user;
    }

}
