package com.jxiang.blog.service;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.vo.param.LoginParam;
import com.jxiang.blog.vo.param.RegisterParam;
import com.jxiang.blog.vo.result.Result;

public interface AuthService {

  /**
   * Login feature take an object of account and password, return token and store
   * it in redis {user:
   * token} test passwords: {password}
   *
   * @param loginParam account & password
   * @return Result
   */
  Result login(LoginParam loginParam);

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
   * @param registerParam object of account, password, nickname, email and avatar
   * @return Result
   */
  Result register(RegisterParam registerParam);

}
