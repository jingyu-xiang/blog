package com.jxiang.blog.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jxiang.blog.service.CommentService;
import com.jxiang.blog.vo.param.CommentParam;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("articles/{id}/comments")
  public Result getArticleComments(@PathVariable final String id) {
    return commentService.getCommentsByArticleId(id);
  }

  @PostMapping("comments/create")
  public Result createComment(@RequestBody final CommentParam commentParam) {
    return commentService.createComment(commentParam);
  }

  @DeleteMapping("comments/delete/{id}")
  public Result deleteComment(@PathVariable("id") final String commentId) {
    return commentService.deleteCommentById(commentId);
  }

}
