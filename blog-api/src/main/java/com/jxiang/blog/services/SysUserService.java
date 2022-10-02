package com.jxiang.blog.services;

import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.vo.SysUserVo;
import com.jxiang.blog.vo.results.Result;
import org.springframework.stereotype.Service;

@Service
public interface SysUserService {

  /**
   * retrieve a user by a given id
   *
   * @param id sysUser's id
   * @return single sysUser
   */
  SysUser findUserById(Long id);

  /**
   * retrieve a user based on its username and password, and reset last login time to current
   *
   * @param account  user account
   * @param password user password
   * @return Result
   */
  SysUser findUserForLogin(String account, String password);

  /**
   * retrieve user's authUserVo according to token
   *
   * @param token jwt token
   * @return Result
   */
  Result findCurrentLoginUserVoByToken(String token);

  /**
   * find sysUser according to account (unique)
   *
   * @param account user account
   * @return Result
   */
  SysUser findUserByAccount(String account);

  /**
   * save sysUser to db
   *
   * @param sysUser new created sysUser object
   */
  void save(SysUser sysUser);

  /**
   * given author id, generate a corresponding sysUserVo
   *
   * @param sysUserId sysUserId
   * @return Result sysUserVo object
   */
  SysUserVo getSysUserVoById(Long sysUserId);

}
