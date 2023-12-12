package com.jxiang.blog.service.impl.user;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.SysUserMapper;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.service.thread.ThreadService;
import com.jxiang.blog.vo.AuthorVo;
import com.jxiang.blog.vo.SysUserVo;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

  private final SysUserMapper sysUserMapper;
  private final AuthService authService;
  private final ThreadService threadService;
  private final SysUserServiceUtils sysUserServiceUtils;

  @Override
  public AuthorVo findAuthorVoById(final Long id) {
    SysUser sysUser = sysUserMapper.selectById(id);
    if (sysUser == null) {
      sysUser = new SysUser();
      sysUser.setNickname("unknown");
      sysUser.setGithub("unknown");
    }
    return sysUserServiceUtils.copyAuthor(sysUser);
  }

  @Override
  public SysUser findUserForLogin(final String account, final String password) {
    final LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper
        .eq(SysUser::getAccount, account)
        .eq(SysUser::getPassword, password)
        .last("LIMIT 1");
    return sysUserMapper.selectOne(queryWrapper);
  }

  @Override
  public Result findCurrentLoginUserVoByToken(final String token) {
    final SysUser sysUser = authService.checkToken(token);

    if (sysUser == null)
      return Result.failure(ErrorCode.TOKEN_INVALID.getCode(), ErrorCode.TOKEN_INVALID.getMsg());

    // use thread pool to change lastLogin, isolated from the main program thread
    threadService.updateLastLogin(sysUser, token, sysUserMapper);

    final SysUserVo sysUserVo = new SysUserVo();
    sysUserVo.setId(String.valueOf(sysUser.getId()));
    BeanUtils.copyProperties(sysUser, sysUserVo);
    sysUserVo.setLastLogin(
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(sysUser.getLastLogin()), ZoneId.systemDefault()).toString());

    return Result.success(sysUserVo);
  }

  @Override
  public SysUser findUserByAccount(final String account) {
    final LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(SysUser::getAccount, account).last("LIMIT 1");
    return sysUserMapper.selectOne(queryWrapper);
  }

  @Override
  public void save(final SysUser sysUser) {
    sysUserMapper.insert(sysUser); // id is auto-generated with snowflakes
  }

}
