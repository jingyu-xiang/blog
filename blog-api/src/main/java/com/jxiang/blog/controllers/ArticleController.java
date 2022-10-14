package com.jxiang.blog.controllers;

import com.jxiang.blog.aop.log.LogAnnotation;
import com.jxiang.blog.services.ArticleService;
import com.jxiang.blog.vo.params.ArticleBodyParam;
import com.jxiang.blog.vo.params.ArticleParam;
import com.jxiang.blog.vo.params.ArticleUpdateParam;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.params.PageParams;
import com.jxiang.blog.vo.results.Result;
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

  @LogAnnotation(module = "article", operator = "get articles") // AOP log
  @PostMapping
  public Result listArticles(@RequestBody PageParams pageParams) {
    return articleService.listArticles(pageParams);
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
