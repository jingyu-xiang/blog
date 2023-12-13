package com.jxiang.blog.vo;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentVo {

  private String id;

  private AuthorVo author;

  private String content;

  private List<CommentVo> children;

  private String createDate;

  private Integer level;

  private AuthorVo toUser;

  private CommentVo toComment;

}