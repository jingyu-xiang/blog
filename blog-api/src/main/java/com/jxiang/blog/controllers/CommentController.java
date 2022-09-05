package com.jxiang.blog.controllers;

import com.jxiang.blog.services.CommentService;
import com.jxiang.blog.vo.params.CommentParam;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("articles/{id}/comments")
    public Result getArticleComments(@PathVariable String id) {
        return commentService.getCommentsByArticleId(id);
    }

    @PostMapping("comments")
    public Result createComment(@RequestBody CommentParam commentParam) {
        return commentService.createComment(commentParam);
    }

    @DeleteMapping("comments/{id}")
    public Result deleteComment(@PathVariable("id") String commentId) {
        return commentService.deleteCommentById(Long.valueOf(commentId));
    }

}
