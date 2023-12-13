package com.jxiang.blog.service.impl.auth;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.util.JwtUtils;
import com.jxiang.blog.vo.param.LoginParam;
import com.jxiang.blog.vo.param.RegisterParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

@Service
@EnableConfigurationProperties
public class AuthServiceImpl implements AuthService {

  private final Environment environment;

  private final JwtUtils jwtUtils;

  private final SysUserService sysUserService;

  private final RedisTemplate<String, String> redisTemplate;

  public AuthServiceImpl(
      final Environment environment,
      final JwtUtils jwtUtils,
      @Lazy final SysUserService sysUserService,
      final RedisTemplate<String, String> redisTemplate) {
    this.environment = environment;
    this.jwtUtils = jwtUtils;
    this.sysUserService = sysUserService;
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Result login(final LoginParam loginParam) {
    final String account = loginParam.getAccount();
    String password = loginParam.getPassword();

    if (account.isBlank() || password.isBlank()) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

    // encode (password + salt)
    password = DigestUtils.md5Hex(password + environment.getProperty("credentials.salt"));
    final SysUser sysUser = sysUserService.findUserForLogin(account, password);

    if (sysUser == null) {
      return Result.failure(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
    }

    final String token = jwtUtils.createToken(sysUser.getId());

    // store token in redis {TOKEN_ey21e123f24=SysUser}
    redisTemplate
        .opsForValue()
        .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
    return Result.success(token);
  }

  @Override
  public SysUser checkToken(final String token) {
    if (token.isBlank()) {
      return null;
    }

    final Map<String, Object> stringObjectMap = jwtUtils.checkToken(token);
    if (stringObjectMap == null) {
      // jwt check failed
      return null;
    }

    final String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
    if (userJson.isBlank()) {
      return null;
    }

    // token is ok, retrieve sysUser obj from redis
    return JSON.parseObject(userJson, SysUser.class);
  }

  @Override
  public Result logout(final String token) {
    final String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);

    redisTemplate.delete("TOKEN_" + token);

    if (!userJson.isBlank()) {
      return Result.success(JSON.parseObject(userJson, SysUser.class).getId());
    }

    return Result.failure(
        ErrorCode.NO_LOGIN.getCode(),
        ErrorCode.NO_LOGIN.getMsg());
  }

  @Override
  @Transactional
  public Result register(final RegisterParam registerParam) {
    final String account = registerParam.getAccount();
    final String password = registerParam.getPassword();
    final String nickname = registerParam.getNickname();
    final String github = registerParam.getGithub();

    if (account.isBlank() || password.isBlank() || nickname.isBlank() || github.isBlank()) {
      return Result.failure(
          ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    // check if sysUser already exists
    SysUser sysUser = sysUserService.findUserByAccount(account);
    if (sysUser != null) {
      return Result.failure(ErrorCode.ACCOUNT_EXISTS.getCode(), ErrorCode.ACCOUNT_EXISTS.getMsg());
    }

    sysUser = SysUser.builder()
        .nickname(nickname)
        .account(account)
        .password(DigestUtils.md5Hex(password + environment.getProperty("credentials.salt")))
        .github(github)
        .createDate(System.currentTimeMillis())
        .lastLogin(System.currentTimeMillis())
        .admin(0) // not admin
        .deleted(0) // not deleted
        .build();

    sysUserService.save(sysUser);

    final String token = jwtUtils.createToken(sysUser.getId());

    // store token in redis {TOKEN_ey21e123f24=SysUser}
    redisTemplate
        .opsForValue()
        .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

    return Result.success(token);
  }

}
