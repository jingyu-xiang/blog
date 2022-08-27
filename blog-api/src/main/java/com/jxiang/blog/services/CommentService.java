package com.jxiang.blog.services;

import com.jxiang.blog.vo.params.CommentParam;
import com.jxiang.blog.vo.results.Result;

public interface CommentService {

    /**
     * given article id, find its comments and the corresponding creators
     * if level = 2, find the level2 comments on the level1 comment, and their creators
     *
     * @param id article id
     * @return comments of the article with article id to be id
     */
    Result getCommentsByArticleId(String id);

    /**
     * comment on an article
     * the creator of comment must log in first
     *
     * @param commentParam {articleId, content, parentId, toUserId}
     * @return Result
     */
    Result createComment(CommentParam commentParam);

}
