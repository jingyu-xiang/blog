package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "ms_article_tag")
public class ArticleTag {

  private Long id;

  private Long articleId;

  private Long tagId;

}
