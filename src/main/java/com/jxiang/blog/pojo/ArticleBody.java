package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@TableName(value = "ms_article_body")
public class ArticleBody {

  private Long id;

  private String content;

  private String contentHtml;

  private Long articleId;

}
