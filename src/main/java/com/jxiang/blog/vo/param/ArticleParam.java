package com.jxiang.blog.vo.param;

import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.TagVo;
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
