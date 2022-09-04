package com.jxiang.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.SysUserMapper;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.services.AuthService;
import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.services.Thread.ThreadService;
import com.jxiang.blog.vo.SysUserVo;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    AuthService authService;

    @Autowired
    ThreadService threadService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("unknown");
        }
        return sysUser;
    }

    @Override
    public SysUserVo getSysUserVoById(Long sysUserId) {
        SysUser sysUser = sysUserMapper.selectById(sysUserId);
        if (sysUser == null) {
            // generate a template for all anonymous users
            sysUser = new SysUser();
            sysUser.setAccount("anonymous user");
            sysUser.setAvatar("unknown.png");
            sysUser.setNickname("unknown");
        }

        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser, sysUserVo); // copy field values of sysUser to sysUserVo if match
        System.out.println(sysUserVo);

        return sysUserVo;
    }

    @Override
    public SysUser findUserForLogin(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
            .eq(SysUser::getAccount, account)
            .eq(SysUser::getPassword, password)
            .last("LIMIT 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findCurrentLoginUserVoByToken(String token) {
        SysUser sysUser = authService.checkToken(token);

        if (sysUser == null) {
            return Result.failure(ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMsg());
        }

        // use thread pool to change lastLogin, isolated from the main program thread
        threadService.updateLastLogin(sysUser, token, sysUserMapper);

        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser, sysUserVo);
        sysUserVo.setLastLogin(new DateTime(sysUser.getLastLogin()).toString("yyyy-MM-dd HH:mm"));

        return Result.success(sysUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SysUser::getAccount, account).last("LIMIT 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        // id is auto-generated with snowflakes
        sysUserMapper.insert(sysUser);
    }


}
