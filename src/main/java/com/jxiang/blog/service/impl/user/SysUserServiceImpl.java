package com.jxiang.blog.service.impl.user;

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
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

  private final SysUserMapper sysUserMapper;
  private final AuthService authService;
  private final ThreadService threadService;
  private final SysUserServiceUtils sysUserServiceUtils;

  @Autowired
  public SysUserServiceImpl(
      SysUserMapper sysUserMapper,
      AuthService authService,
      ThreadService threadService,
      SysUserServiceUtils sysUserServiceUtils
  ) {
    this.sysUserMapper = sysUserMapper;
    this.authService = authService;
    this.threadService = threadService;
    this.sysUserServiceUtils = sysUserServiceUtils;
  }

  @Override
  public AuthorVo findAuthorVoById(Long id) {
    SysUser sysUser = sysUserMapper.selectById(id);
    if (sysUser == null) {
      sysUser = new SysUser();
      sysUser.setNickname("unknown");
      sysUser.setGithub("unknown");
    }
    return sysUserServiceUtils.copyAuthor(sysUser);
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
      return Result.failure(ErrorCode.TOKEN_INVALID.getCode(),
          ErrorCode.TOKEN_INVALID.getMsg());
    }

    // use thread pool to change lastLogin, isolated from the main program thread
    threadService.updateLastLogin(sysUser, token, sysUserMapper);

    SysUserVo sysUserVo = new SysUserVo();
    sysUserVo.setId(String.valueOf(sysUser.getId()));
    BeanUtils.copyProperties(sysUser, sysUserVo);
    sysUserVo.setLastLogin(
        new DateTime(sysUser.getLastLogin()).toString("yyyy-MM-dd HH:mm"));

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
