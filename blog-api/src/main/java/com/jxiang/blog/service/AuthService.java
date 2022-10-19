package com.jxiang.blog.service;

import com.jxiang.common.pojo.SysUser;
import com.jxiang.common.vo.params.LoginParams;
import com.jxiang.common.vo.params.RegisterParams;
import com.jxiang.common.vo.results.Result;

public interface AuthService {

  /**
   * Login feature take an object of account and password, return token and store it in redis {user:
   * token} test passwords: {password}
   *
   * @param loginParams account & password
   * @return Result
   */
  Result login(LoginParams loginParams);

  /**
   * Check if a jwt token is valid or not
   *
   * @param token jwt token
   * @return SysUser
   */
  SysUser checkToken(String token);

  /**
   * SysUser logout, remove {TOKEN_token: sysUser} from redis store
   *
   * @param token jwt token
   * @return Result
   */
  Result logout(String token);

  /**
   * Create a sysUser with account, password, nickname
   *
   * @param registerParams object of account, password, nickname, email and avatar
   * @return Result
   */
  Result register(RegisterParams registerParams);

}
