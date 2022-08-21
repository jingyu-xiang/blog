package com.jxiang.blog.controllers;

import com.jxiang.blog.services.CommentService;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/articles")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("{id}/comments")
    public Result getArticleComments(@PathVariable String id) {
        return commentService.getCommentsByArticleId(id);
    }

}
