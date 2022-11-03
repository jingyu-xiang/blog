package com.jxiang.blog.service;


import com.jxiang.blog.vo.param.CommentParam;
import com.jxiang.blog.vo.result.Result;

public interface CommentService {

  /**
   * given article id, find its comments and the corresponding creators if level = 2, find the
   * level2 comments on the level1 comment, and their creators
   *
   * @param id article id
   * @return comments of the article with article id to be id
   */
  Result getCommentsByArticleId(String id);

  /**
   * comment on an article the creator of comment must log in first if the comment is level 1, set
   * parentId = -1 and toUser = -1 if the comment is level 2, set parentId to level1 comment and
   * toUser to level1 comment author
   *
   * @param commentParam {articleId, content, parentId, toUserId}
   * @return Result
   */
  Result createComment(CommentParam commentParam);

  /**
   * delete a comment by its author, providing commentId if the comment is level 2, just delete it
   * by set deleted to true if the comment is level 1, delete it's level 2 comments as well by set
   * deleted to true
   *
   * @param commentId comment id
   * @return Result
   */
  Result deleteCommentById(Long commentId);

  /**
   * delete an article's all comments
   *
   * @param articleId article id
   * @return Result
   */
  Boolean deleteArticleComments(Long articleId);

}
