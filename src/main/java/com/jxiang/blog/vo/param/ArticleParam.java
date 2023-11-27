package com.jxiang.blog.vo.param;

import java.util.List;
import lombok.Data;

@Data
public class ArticleParam {

  private ArticleBodyParam body;

  private String categoryId;

  private String summary;

  private List<String> tagIds;

  private String title;

}
