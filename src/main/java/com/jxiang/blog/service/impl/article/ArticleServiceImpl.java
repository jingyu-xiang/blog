package com.jxiang.blog.service.impl.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jxiang.blog.aop.cache.MySpringCache;
import com.jxiang.blog.dao.mapper.ArticleBodyMapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.ArticleTagMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.pojo.ArticleBody;
import com.jxiang.blog.pojo.ArticleTag;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.service.ArticleService;
import com.jxiang.blog.service.CommentService;
import com.jxiang.blog.service.thread.ThreadService;
import com.jxiang.blog.util.SysUserThreadLocal;
import com.jxiang.blog.vo.ArticleVo;
import com.jxiang.blog.vo.param.ArticleBodyParam;
import com.jxiang.blog.vo.param.ArticleParam;
import com.jxiang.blog.vo.param.ArticleUpdateParam;
import com.jxiang.blog.vo.param.LimitParam;
import com.jxiang.blog.vo.param.PageParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleMapper articleMapper;

  private final CommentService commentService;

  private final ThreadService threadService;

  private final ArticleTagMapper articleTagMapper;

  private final ArticleBodyMapper articleBodyMapper;

  private final ArticleServiceUtils articleServiceUtils;

  @Override
  @MySpringCache(name = "listArticles")
  public Result listArticles(final PageParam pageParam) {
    final Page<Article> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
    final LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

    if (pageParam.getUserId() != null) {
      final Long userId = Long.valueOf(pageParam.getUserId());
      queryWrapper.eq(Article::getAuthorId, userId);
    }

    if (pageParam.getCategoryId() != null) {
      final Long categoryId = Long.valueOf(pageParam.getCategoryId());
      queryWrapper.eq(Article::getCategoryId, categoryId);
    }

    if (pageParam.getTagId() != null) {
      final Long tagId = Long.valueOf(pageParam.getTagId());

      // article_id 1 -- * tag_id
      final LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
      articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, tagId);
      final List<ArticleTag> articleTagList = articleTagMapper.selectList(
          articleTagLambdaQueryWrapper);

      final List<Long> articleIdList = new ArrayList<>();
      articleTagList.forEach(
          articleTag -> articleIdList.add(articleTag.getArticleId()));

      if (articleIdList.size() > 0) {
        queryWrapper.in(Article::getId, articleIdList);
      }
    }

    queryWrapper // order by create_date DESC & weight DESC
        .orderByDesc(Article::getCreateDate);

    final Page<Article> articlePage = articleMapper.selectPage(
        page,
        queryWrapper);

    final List<ArticleVo> articleVoList = articleServiceUtils.copyList(
        articlePage.getRecords(),
        true,
        true);

    return articleVoList.size() != 0
        ? Result.success(articleVoList)
        : Result.success("No article yet");
  }

  @Override
  @MySpringCache(name = "listHotArticles")
  public Result listHotArticles(final LimitParam limitParam) {
    final LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper // select id, title, view_counts from ms_article order by view_counts desc limit
        // {limit}
        .orderByDesc(Article::getViewCounts)
        .select(Article::getId, Article::getTitle, Article::getViewCounts)
        .last("LIMIT " + limitParam.getLimit());

    final List<Article> articles = articleMapper.selectList(queryWrapper);

    final List<ArticleVo> articleVoList = articleServiceUtils.copyList(
        articles, false, false);

    return articleVoList.size() != 0
        ? Result.success(articleVoList)
        : Result.success("No article Yet");
  }

  @Override
  @MySpringCache(name = "listNewArticles")
  public Result listNewArticles(final LimitParam limitParam) {
    final LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper // select id, title, createDate from ms_article order by create_date desc limit
        // {limit}
        .orderByDesc(Article::getCreateDate)
        .select(Article::getId, Article::getTitle, Article::getCreateDate)
        .last("LIMIT " + limitParam.getLimit());

    final List<Article> articles = articleMapper.selectList(queryWrapper);

    final List<ArticleVo> articleVoList = articleServiceUtils.copyList(
        articles, false, false);

    return articleVoList.size() != 0
        ? Result.success(articleVoList)
        : Result.success("No article Yet");
  }

  @Override
  @MySpringCache(name = "listArchiveSummary")
  public Result listArchiveSummary() {
    return Result.success(articleMapper.listArchiveSummary());
  }

  @Override
  public Result findArticleById(final Long articleId) {
    final Article article = articleMapper.selectById(articleId);

    if (article == null) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    final ArticleVo articleVo = articleServiceUtils.copy(
        article, true, true, true, true);

    // use thread pool to process the add review count operation, isolated from the
    // main program
    // thread
    threadService.updateArticleViewCount(articleMapper, article);

    return Result.success(articleVo);
  }

  @Override
  public Result createArticle(final ArticleParam articleParam) {
    final SysUser sysUser = SysUserThreadLocal.get();

    // article basic fields
    final Article article = new Article();
    article.setAuthorId(sysUser.getId());
    article.setViewCounts(0);
    article.setTitle(articleParam.getTitle());
    article.setSummary(articleParam.getSummary());
    article.setCommentCounts(0);
    article.setCreateDate(System.currentTimeMillis());

    // article category
    article.setCategoryId(Long.valueOf(articleParam.getCategoryId()));

    articleMapper.insert(article);

    // article tags
    final List<String> tagIds = articleParam.getTagIds();
    if (tagIds != null && tagIds.size() > 0) {
      for (final String tagId : tagIds) {
        final ArticleTag articleTag = new ArticleTag();
        articleTag.setTagId(Long.valueOf(tagId));
        articleTag.setArticleId(article.getId());
        articleTagMapper.insert(articleTag);
      }
    }

    // article body
    final ArticleBody articleBody = new ArticleBody();
    articleBody.setArticleId(article.getId());
    articleBody.setContent(articleParam.getBody().getContent());
    articleBody.setContentHtml(articleParam.getBody().getContentHtml());
    articleBodyMapper.insert(articleBody);
    article.setBodyId(articleBody.getId());

    // update article
    articleMapper.updateById(article);

    return Result.success(article);
  }

  @Override
  @Transactional
  public Result deleteArticleById(final Long articleId) {
    final Article articleToDelete = articleMapper.selectById(articleId);

    if (articleToDelete == null) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    final SysUser requestUser = SysUserThreadLocal.get();

    if (requestUser.getAdmin() == 1 || articleToDelete.getAuthorId().equals(requestUser.getId())) {
      articleMapper.deleteById(articleToDelete);

      // delete article's comments
      final boolean deleted = commentService.deleteArticleComments(articleId);

      if (deleted) {
        return Result.success(articleToDelete.getId());
      }

      return Result.failure(
          ErrorCode.SYSTEM_ERROR.getCode(),
          ErrorCode.SYSTEM_ERROR.getMsg());
    }

    return Result.failure(
        ErrorCode.NO_PERMISSION.getCode(),
        ErrorCode.NO_PERMISSION.getMsg());
  }

  @Override
  @Transactional
  public Result updateArticleBodyById(final Long articleId, final ArticleBodyParam articleBody) {
    final LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ArticleBody::getArticleId, articleId).last("LIMIT " + 1);
    final ArticleBody articleBodyToUpdate = articleBodyMapper.selectOne(queryWrapper);
    final Article articleToUpdate = articleMapper.selectById(articleId);

    if (articleToUpdate == null) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    if (!articleToUpdate.getAuthorId()
        .equals(SysUserThreadLocal.get().getId())) {
      return Result.failure(
          ErrorCode.NO_LOGIN.getCode(),
          ErrorCode.NO_LOGIN.getMsg());
    }

    if (articleBodyToUpdate == null) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    final String content = articleBody.getContent();
    final String contentHtml = articleBody.getContentHtml();

    // not update if the content has not changed
    if (content.equals(articleBodyToUpdate.getContent()) &&
        contentHtml.equals(articleBodyToUpdate.getContentHtml())) {
      return Result.failure(
          ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    articleBodyToUpdate.setContent(content);
    articleBodyToUpdate.setContentHtml(contentHtml);

    final int success = articleBodyMapper.updateById(articleBodyToUpdate);

    return success == 1
        ? Result.success("Success")
        : Result.failure(
            ErrorCode.SYSTEM_ERROR.getCode(),
            ErrorCode.SYSTEM_ERROR.getMsg());
  }

  @Override
  @Transactional
  public Result updateArticleById(
      final Long articleId,
      final ArticleUpdateParam articleUpdateParam) {
    final Article articleToUpdate = articleMapper.selectById(articleId);

    if (articleToUpdate == null) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    if (!articleToUpdate.getAuthorId()
        .equals(SysUserThreadLocal.get().getId())) {
      return Result.failure(
          ErrorCode.NO_LOGIN.getCode(),
          ErrorCode.NO_LOGIN.getMsg());
    }

    final String title = articleUpdateParam.getTitle();
    final String summary = articleUpdateParam.getSummary();

    if (title == null && summary == null) {
      return Result.failure(
          ErrorCode.PARAMS_ERROR.getCode(),
          ErrorCode.PARAMS_ERROR.getMsg());
    }

    if (title != null) {
      if (!title.equals("") && !title.equals(articleToUpdate.getTitle())) {
        articleToUpdate.setTitle(title);
      } else {
        return Result.failure(
            ErrorCode.PARAMS_ERROR.getCode(),
            ErrorCode.PARAMS_ERROR.getMsg());
      }
    }

    if (summary != null) {
      if (!summary.equals("") && !summary.equals(
          articleToUpdate.getSummary())) {
        articleToUpdate.setSummary(summary);
      } else {
        return Result.failure(
            ErrorCode.PARAMS_ERROR.getCode(),
            ErrorCode.PARAMS_ERROR.getMsg());
      }
    }

    final int success = articleMapper.updateById(articleToUpdate);

    final Map<String, String> res = new HashMap<>();
    res.put("title", articleToUpdate.getTitle());
    res.put("summary", articleToUpdate.getSummary());

    return success == 1
        ? Result.success(res)
        : Result.failure(
            ErrorCode.SYSTEM_ERROR.getCode(),
            ErrorCode.SYSTEM_ERROR.getMsg());
  }

  @Override
  @MySpringCache(name = "listSearchedArticles")
  public Result listSearchedArticles(final String queryString, final PageParam pageParam) {
    if (StringUtils.isEmpty(queryString)) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

    final Page<Article> page = new Page<>(pageParam.getPage(), pageParam.getPageSize());
    final Page<Article> articlePage = articleMapper.queryFullText(page, queryString);
    final List<ArticleVo> articleVoList = articleServiceUtils.copyList(
        articlePage.getRecords(),
        true,
        true);

    return articleVoList.size() != 0
        ? Result.success(articleVoList)
        : Result.success("No article yet");
  }

}
