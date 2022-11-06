package com.jxiang.blog.controller;

import com.jxiang.blog.service.CommentService;
import com.jxiang.blog.vo.param.CommentParam;
import com.jxiang.blog.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class CommentController {

  @Autowired
  CommentService commentService;

  @PostMapping("articles/{id}/comments")
  public Result getArticleComments(@PathVariable String id) {
    return commentService.getCommentsByArticleId(id);
  }

  @PostMapping("comments/create")
  public Result createComment(@RequestBody CommentParam commentParam) {
    return commentService.createComment(commentParam);
  }

  @DeleteMapping("comments/delete/{id}")
  public Result deleteComment(@PathVariable("id") String commentId) {
    return commentService.deleteCommentById(commentId);
  }

}
