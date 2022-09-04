package com.jxiang.blog.controllers;

import com.jxiang.blog.aop.log.LogAnnotation;
import com.jxiang.blog.services.ArticleService;
import com.jxiang.blog.vo.params.ArticleParam;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.params.PageParams;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result retrieveMostPopularArticles(@RequestBody LimitParam limitParam) {
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

    @PostMapping("publish")
    public Result publishArticle(@RequestBody ArticleParam articleParam) {
        return articleService.createArticle(articleParam);
    }

    @DeleteMapping("{id}")
    public Result deleteArticle(@PathVariable("id") Long articleId) {
        // Todo
        return null;
    }

}
