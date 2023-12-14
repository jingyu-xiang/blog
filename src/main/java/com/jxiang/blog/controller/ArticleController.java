package com.jxiang.blog.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxiang.blog.aop.log.Log;
import com.jxiang.blog.service.ArticleService;
import com.jxiang.blog.vo.param.ArticleBodyParam;
import com.jxiang.blog.vo.param.ArticleParam;
import com.jxiang.blog.vo.param.ArticleUpdateParam;
import com.jxiang.blog.vo.param.LimitParam;
import com.jxiang.blog.vo.param.PageParam;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;

  @Log(module = "article", operator = "get articles") // AOP log
  @PostMapping
  public Result listArticles(@RequestBody final PageParam pageParam) {
    return articleService.listArticles(pageParam);
  }

  @PostMapping("fulltext/{text}")
  public Result listSearchedArticles(
      @PathVariable("text") final String queryString,
      @RequestBody final PageParam pageParam) {
    return articleService.listSearchedArticles(queryString, pageParam);
  }

  @PostMapping("hots")
  public Result retrieveMostPopularArticles(
      @RequestBody final LimitParam limitParam) {
    return articleService.listHotArticles(limitParam);
  }

  @PostMapping("news")
  public Result retrieveMostRecentArticles(@RequestBody final LimitParam limitParam) {
    return articleService.listNewArticles(limitParam);
  }

  @PostMapping("archives")
  public Result listArchiveSummary() {
    return articleService.listArchiveSummary();
  }

  @PostMapping("{id}")
  public Result findArticleById(@PathVariable("id") final Long articleId) {
    return articleService.findArticleById(articleId);
  }

  @PostMapping("create")
  public Result publishArticle(@RequestBody final ArticleParam articleParam) {
    return articleService.createArticle(articleParam);
  }

  @DeleteMapping("delete/{id}")
  public Result deleteArticleById(@PathVariable("id") final Long articleId) {
    return articleService.deleteArticleById(articleId);
  }

  @PatchMapping("update/body/{id}")
  public Result updateArticleBodyById(
      @PathVariable("id") final Long articleId,
      @RequestBody final ArticleBodyParam articleBody) {
    return articleService.updateArticleBodyById(articleId, articleBody);
  }

  @PatchMapping("update/{id}")
  public Result updateArticleById(
      @PathVariable("id") final Long articleId,
      @RequestBody final ArticleUpdateParam articleUpdateParam) {
    return articleService.updateArticleById(articleId, articleUpdateParam);
  }

}
