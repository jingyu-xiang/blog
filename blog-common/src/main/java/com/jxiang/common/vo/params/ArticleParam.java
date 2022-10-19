package com.jxiang.common.vo.params;

import com.jxiang.common.vo.CategoryVo;
import com.jxiang.common.vo.TagVo;
import java.util.List;
import lombok.Data;

@Data
public class ArticleParam {

  private ArticleBodyParam body;

  private CategoryVo category;

  private String summary;

  private List<TagVo> tags;

  private String title;

}
