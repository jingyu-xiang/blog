package com.jxiang.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ms_article_body")
public class ArticleBody {

    private Long id;

    private String content;

    private String contentHtml;

    private Long articleId;

}
