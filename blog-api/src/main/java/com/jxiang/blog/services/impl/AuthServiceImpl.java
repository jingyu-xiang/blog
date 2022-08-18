package com.jxiang.blog.services.impl;

import com.alibaba.fastjson.JSON;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.services.AuthService;
import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.utils.JwtUtils;
import com.jxiang.blog.vo.params.LoginParams;
import com.jxiang.blog.vo.params.RegisterParams;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String SALT = "89j3@129%";

    @Lazy
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Result login(LoginParams loginParams) {
        String account = loginParams.getAccount();
        String password = loginParams.getPassword();

        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        // encode (password + salt)
        password = DigestUtils.md5Hex(password + SALT);

        SysUser sysUser = sysUserService.findAuthUserForLogin(account, password);

        if (sysUser == null) {
            return Result.failure(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JwtUtils.createToken(sysUser.getId());

        // store token in redis {TOKEN_ey21e123f24=SysUser}
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }

        Map<String, Object> stringObjectMap = JwtUtils.checkToken(token);
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

        return Result.failure(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
    }

    @Override
    public Result register(RegisterParams registerParams) {
        String account = registerParams.getAccount();
        String password = registerParams.getPassword();
        String nickname = registerParams.getNickname();
        String email = registerParams.getEmail();

        if (StringUtils.isBlank(account)
            || StringUtils.isBlank(password)
            || StringUtils.isBlank(nickname)
            || StringUtils.isBlank(email)
        ) {
            return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }

        // check if sysUser already exists
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null) {
            return Result.failure(ErrorCode.ACCOUNT_EXISTS.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + SALT));
        sysUser.setEmail(email);
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(0); // not admin
        sysUser.setDeleted(0); // not deleted
        sysUser.setStatus("");
        sysUserService.save(sysUser);

        String token = JwtUtils.createToken(sysUser.getId());

        // store token in redis {TOKEN_ey21e123f24=SysUser}
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);

        return Result.success(token);
    }

}
