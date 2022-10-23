package com.jxiang.blog.controller;

import com.jxiang.blog.aop.log.Log;
import com.jxiang.blog.service.ArticleService;
import com.jxiang.common.vo.param.ArticleBodyParam;
import com.jxiang.common.vo.param.ArticleParam;
import com.jxiang.common.vo.param.ArticleUpdateParam;
import com.jxiang.common.vo.param.LimitParam;
import com.jxiang.common.vo.param.PageParam;
import com.jxiang.common.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/articles")
public class ArticleController {

  @Autowired
  ArticleService articleService;

  @Log(module = "article", operator = "get articles") // AOP log
  @PostMapping
  public Result listArticles(@RequestBody PageParam pageParam) {
    return articleService.listArticles(pageParam);
  }

  @PostMapping("fulltext/{text}")
  public Result listSearchedArticles(@PathVariable("text") String queryString,
      @RequestBody PageParam pageParam) {
    return articleService.listSearchedArticles(queryString, pageParam);
  }

  @PostMapping("hots")
  public Result retrieveMostPopularArticles(
      @RequestBody LimitParam limitParam) {
    return articleService.listHotArticles(limitParam);
  }

  @PostMapping("news")
  public Result retrieveMostRecentArticles(@RequestBody LimitParam limitParam) {
    return articleService.listNewArticles(limitParam);
  }

  @PostMapping("archives")
  public Result listArchiveSummary() {
    return articleService.listArchiveSummary();
  }

  @PostMapping("{id}")
  public Result findArticleById(@PathVariable("id") Long articleId) {
    return articleService.findArticleById(articleId);
  }

  @PostMapping("create")
  public Result publishArticle(@RequestBody ArticleParam articleParam) {
    return articleService.createArticle(articleParam);
  }

  @DeleteMapping("delete/{id}")
  public Result deleteArticleById(@PathVariable("id") Long articleId) {
    return articleService.deleteArticleById(articleId);
  }

  @PatchMapping("update/body/{id}")
  public Result updateArticleBodyById(
      @PathVariable("id") Long articleId,
      @RequestBody ArticleBodyParam articleBody
  ) {
    return articleService.updateArticleBodyById(articleId, articleBody);
  }

  @PatchMapping("update/{id}")
  public Result updateArticleById(
      @PathVariable("id") Long articleId,
      @RequestBody ArticleUpdateParam articleUpdateParam
  ) {
    return articleService.updateArticleById(articleId, articleUpdateParam);
  }

}
