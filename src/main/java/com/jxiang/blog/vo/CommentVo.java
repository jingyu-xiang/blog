package com.jxiang.blog.vo;

import java.util.List;
import lombok.Data;

@Data
public class CommentVo {

  private String id;

  private AuthorVo author;

  private String content;

  private List<CommentVo> children;

  private String createDate;

  private Integer level;

  private AuthorVo toUser;

}