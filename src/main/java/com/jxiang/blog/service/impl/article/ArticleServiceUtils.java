package com.jxiang.blog.service.impl.article;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jxiang.blog.dao.mapper.ArticleBodyMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.pojo.ArticleBody;
import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.service.TagService;
import com.jxiang.blog.vo.ArticleBodyVo;
import com.jxiang.blog.vo.ArticleVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceUtils {

  private final CategoryService categoryService;
  private final TagService tagService;
  private final SysUserService sysUserService;
  private final ArticleBodyMapper articleBodyMapper;

  List<ArticleVo> copyList(
      final List<Article> records,
      final boolean isTagsRequired,
      final boolean isAuthorRequired) {
    final List<ArticleVo> articleVoList = new ArrayList<>();
    for (final Article record : records) {
      // author and tags are required
      articleVoList.add(
          copy(record, isTagsRequired, isAuthorRequired, false, true));
    }
    return articleVoList;
  }

  ArticleVo copy(
      final Article article,
      final boolean isTagsRequired,
      final boolean isAuthorRequired,
      final boolean isBody,
      final boolean isCategory) {
    final ArticleVo articleVo = new ArticleVo();
    articleVo.setId(String.valueOf(article.getId()));

    // copy properties of article to articleVo, set field of articleVo to null if it
    // is not in
    // article
    BeanUtils.copyProperties(article, articleVo);

    articleVo.setCreateDate(
        new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

    if (isBody) {
      final Long bodyId = article.getBodyId();
      articleVo.setBody(findArticleBodyById(bodyId));
    }

    if (isCategory) { // article category
      final Long categoryId = article.getCategoryId();
      articleVo.setCategory(categoryService.findCategoryById(categoryId));
    }

    if (isTagsRequired) { // article tags
      final Long articleId = Long.valueOf(articleVo.getId());
      articleVo.setTags(tagService.findTagsByArticleId(articleId));
    }

    if (isAuthorRequired) {
      final Long authorId = article.getAuthorId();
      articleVo.setAuthor(sysUserService.findAuthorVoById(authorId));
    }

    return articleVo;
  }

  private ArticleBodyVo findArticleBodyById(final Long bodyId) {
    final ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
    final ArticleBodyVo articleBodyVo = new ArticleBodyVo();
    articleBodyVo.setContent(articleBody.getContent());
    articleBodyVo.setContentHtml(articleBody.getContentHtml());
    return articleBodyVo;
  }

}
