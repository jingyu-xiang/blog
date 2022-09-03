package com.jxiang.blog.vo.params;

import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

}