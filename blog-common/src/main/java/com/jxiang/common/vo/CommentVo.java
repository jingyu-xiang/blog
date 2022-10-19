package com.jxiang.common.vo;

import java.util.List;
import lombok.Data;

@Data
public class CommentVo {

  private String id;

  private SysUserVo author;

  private String content;

  private List<CommentVo> children;

  private String createDate;

  private Integer level;

  private SysUserVo toUser;

}