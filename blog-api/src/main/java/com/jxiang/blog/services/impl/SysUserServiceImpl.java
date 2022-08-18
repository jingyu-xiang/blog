package com.jxiang.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.SysUserMapper;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.services.AuthService;
import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.vo.AuthUserVo;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    AuthService authService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            user = new SysUser();
            user.setNickname("unknown");
        }
        return user;
    }

    @Override
    public SysUser findAuthUserForLogin(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
            .eq(SysUser::getAccount, account)
            .eq(SysUser::getPassword, password)
            .select(SysUser::getId, SysUser::getAccount, SysUser::getAvatar, SysUser::getNickname)
            .last("LIMIT 1");

        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        if (sysUser != null) {
            sysUser.setLastLogin(System.currentTimeMillis());
            sysUserMapper.updateById(sysUser);
            return sysUser;
        }

        return null;
    }

    @Override
    public Result findUserByToken(String token) {


        SysUser sysUser = authService.checkToken(token);

        if (sysUser == null) {
            return Result.failure(ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMsg());
        }

        AuthUserVo authUserVo = new AuthUserVo();
        BeanUtils.copyProperties(sysUser, authUserVo);

        return Result.success(authUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(SysUser::getAccount, account).last("LIMIT 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        // id is auto-generated with 雪花算法
        sysUserMapper.insert(sysUser);
    }

}
