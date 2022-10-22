package com.jxiang.blog.service.impl.auth;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.service.AuthService;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.util.beans.JwtUtils;
import com.jxiang.common.pojo.SysUser;
import com.jxiang.common.vo.param.LoginParam;
import com.jxiang.common.vo.param.RegisterParam;
import com.jxiang.common.vo.result.ErrorCode;
import com.jxiang.common.vo.result.Result;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableConfigurationProperties
public class AuthServiceImpl implements AuthService {

  private final Environment environment;
  private final JwtUtils jwtUtils;
  private final SysUserService sysUserService;
  private final RedisTemplate<String, String> redisTemplate;

  @Autowired
  public AuthServiceImpl(
      Environment environment,
      JwtUtils jwtUtils,
      @Lazy SysUserService sysUserService,
      RedisTemplate<String, String> redisTemplate
  ) {
    this.environment = environment;
    this.jwtUtils = jwtUtils;
    this.sysUserService = sysUserService;
    this.redisTemplate = redisTemplate;
  }


  @Override
  public Result login(LoginParam loginParam) {
    String account = loginParam.getAccount();
    String password = loginParam.getPassword();

    if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    // encode (password + salt)
    password = DigestUtils.md5Hex(
        password + environment.getProperty("credentials.salt"));

    SysUser sysUser = sysUserService.findUserForLogin(account, password);

    if (sysUser == null) {
      return Result.failure(
          ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),
          ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg()
      );
    }

    String token = jwtUtils.createToken(sysUser.getId());

    // store token in redis {TOKEN_ey21e123f24=SysUser}
    redisTemplate
        .opsForValue()
        .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

    return Result.success(token);
  }

  @Override
  public SysUser checkToken(String token) {
    if (StringUtils.isBlank(token)) {
      return null;
    }

    Map<String, Object> stringObjectMap = jwtUtils.checkToken(token);
    if (stringObjectMap == null) {
      // jwt check failed
      return null;
    }

    String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
    if (StringUtils.isBlank(userJson)) {
      return null;
    }

    // token is ok, retrieve sysUser obj from redis
    return JSON.parseObject(userJson, SysUser.class);
  }

  @Override
  public Result logout(String token) {
    String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);

    redisTemplate.delete("TOKEN_" + token);

    if (!StringUtils.isBlank(userJson)) {
      return Result.success(JSON.parseObject(userJson, SysUser.class).getId());
    }

    return Result.failure(ErrorCode.NO_LOGIN.getCode(),
        ErrorCode.NO_LOGIN.getMsg());
  }

  @Override
  @Transactional
  public Result register(RegisterParam registerParam) {
    String account = registerParam.getAccount();
    String password = registerParam.getPassword();
    String nickname = registerParam.getNickname();
    String email = registerParam.getEmail();
    String avatarUrl = registerParam.getAvatar();

    if (StringUtils.isBlank(account)
        || StringUtils.isBlank(password)
        || StringUtils.isBlank(nickname)
        || StringUtils.isBlank(email)
    ) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    // check if sysUser already exists
    SysUser sysUser = sysUserService.findUserByAccount(account);
    if (sysUser != null) {
      return Result.failure(ErrorCode.ACCOUNT_EXISTS.getCode(),
          ErrorCode.ACCOUNT_EXISTS.getMsg());
    }

    sysUser = new SysUser();
    sysUser.setNickname(nickname);
    sysUser.setAccount(account);
    sysUser.setPassword(DigestUtils.md5Hex(
        password + environment.getProperty("credentials.salt")));
    sysUser.setEmail(email);
    sysUser.setCreateDate(System.currentTimeMillis());
    sysUser.setLastLogin(System.currentTimeMillis());
    sysUser.setAvatar(avatarUrl);
    sysUser.setAdmin(0); // not admin
    sysUser.setDeleted(0); // not deleted
    sysUserService.save(sysUser);

    String token = jwtUtils.createToken(sysUser.getId());

    // store token in redis {TOKEN_ey21e123f24=SysUser}
    redisTemplate
        .opsForValue()
        .set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

    return Result.success(token);
  }

}
