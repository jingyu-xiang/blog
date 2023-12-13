package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "ms_comment")
public class Comment {

  private Long id;

  private String content;

  private Long createDate;

  private Integer level;

  private Long articleId;

  private Long authorId;

  private Long parentId;

  private Long toUid;

  private Long toCommentId;

  @TableLogic
  private Boolean deleted;

}
