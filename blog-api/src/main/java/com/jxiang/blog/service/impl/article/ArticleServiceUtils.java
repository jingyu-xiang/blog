package com.jxiang.blog.service.impl.article;

import com.jxiang.blog.dao.mapper.ArticleBodyMapper;
import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.service.TagService;
import com.jxiang.common.pojo.Article;
import com.jxiang.common.pojo.ArticleBody;
import com.jxiang.common.vo.ArticleBodyVo;
import com.jxiang.common.vo.ArticleVo;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceUtils {

  private final CategoryService categoryService;
  private final TagService tagService;
  private final SysUserService sysUserService;
  private final ArticleBodyMapper articleBodyMapper;

  @Autowired
  public ArticleServiceUtils(
      CategoryService categoryService,
      TagService tagService,
      SysUserService sysUserService,
      ArticleBodyMapper articleBodyMapper
  ) {
    this.categoryService = categoryService;
    this.tagService = tagService;
    this.sysUserService = sysUserService;
    this.articleBodyMapper = articleBodyMapper;
  }

  List<ArticleVo> copyList(List<Article> records,
      boolean isTagsRequired,
      boolean isAuthorRequired) {
    List<ArticleVo> articleVoList = new ArrayList<>();
    for (Article record : records) {
      // author and tags are required
      articleVoList.add(
          copy(record, isTagsRequired, isAuthorRequired, false, false)
      );
    }
    return articleVoList;
  }

  ArticleVo copy(
      Article article,
      boolean isTagsRequired,
      boolean isAuthorRequired,
      boolean isBody,
      boolean isCategory
  ) {
    ArticleVo articleVo = new ArticleVo();
    articleVo.setId(String.valueOf(article.getId()));

    // copy properties of article to articleVo, set field of articleVo to null if it is not in article
    BeanUtils.copyProperties(article, articleVo);

    articleVo.setCreateDate(
        new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

    if (isBody) {
      Long bodyId = article.getBodyId();
      articleVo.setBody(findArticleBodyById(bodyId));
    }

    if (isCategory) { // article category
      Long categoryId = article.getCategoryId();
      articleVo.setCategory(categoryService.findCategoryById(categoryId));
    }

    if (isTagsRequired) { // article tags
      Long articleId = Long.valueOf(articleVo.getId());
      articleVo.setTags(tagService.findTagsByArticleId(articleId));
    }

    if (isAuthorRequired) {
      Long authorId = article.getAuthorId();
      articleVo.setAuthor(sysUserService.findAuthorVoById(authorId));
    }

    return articleVo;
  }

  private ArticleBodyVo findArticleBodyById(Long bodyId) {
    ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
    ArticleBodyVo articleBodyVo = new ArticleBodyVo();
    articleBodyVo.setContent(articleBody.getContent());
    articleBodyVo.setContentHtml(articleBody.getContentHtml());
    return articleBodyVo;
  }

}
