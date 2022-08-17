package com.jxiang.blog.controllers;

import com.jxiang.blog.services.ArticleService;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.params.PageParams;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;

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
    public Result listArchives() {
        return articleService.listArchives();
    }

}
