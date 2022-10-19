package com.jxiang.blog.service.impl.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.CommentMapper;
import com.jxiang.blog.service.CommentService;
import com.jxiang.blog.service.thread.ThreadService;
import com.jxiang.blog.util.statics.SysUserThreadLocal;
import com.jxiang.common.pojo.Article;
import com.jxiang.common.pojo.Comment;
import com.jxiang.common.pojo.SysUser;
import com.jxiang.common.vo.CommentVo;
import com.jxiang.common.vo.param.CommentParam;
import com.jxiang.common.vo.result.ErrorCode;
import com.jxiang.common.vo.result.Result;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentMapper commentMapper;
  private final ArticleMapper articleMapper;
  private final ThreadService threadService;
  private final CommentServiceUtils commentServiceUtils;

  @Autowired
  public CommentServiceImpl(
      CommentMapper commentMapper,
      ArticleMapper articleMapper,
      ThreadService threadService,
      CommentServiceUtils commentServiceUtils
  ) {
    this.commentMapper = commentMapper;
    this.articleMapper = articleMapper;
    this.threadService = threadService;
    this.commentServiceUtils = commentServiceUtils;
  }

  @Override
  public Result getCommentsByArticleId(String id) {
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper // parent comments
        .eq(Comment::getArticleId, id)
        .eq(Comment::getLevel, 1);

    List<Comment> comments = commentMapper.selectList(queryWrapper);

    List<CommentVo> commentVoList = commentServiceUtils.copyList(comments);

    return Result.success(commentVoList);
  }

  @Override
  public Result createComment(CommentParam commentParam) {

    if (commentParam.getArticleId() == null) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    Article article;

    try {
      article = articleMapper.selectById(commentParam.getArticleId());
    } catch (Exception e) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    SysUser author = SysUserThreadLocal.get();
    Comment comment = new Comment();
    comment.setArticleId(Long.valueOf(commentParam.getArticleId()));
    comment.setAuthorId(author.getId());
    comment.setContent(commentParam.getContent());
    comment.setCreateDate(System.currentTimeMillis());

    Long parentId = Long.valueOf(commentParam.getParent());
    Long toUserId = Long.valueOf(commentParam.getToUserId());
    if (parentId == -1L) {
      // level1
      comment.setLevel(1);
      comment.setParentId(-1L);
      comment.setToUid(-1L);
    } else {
      // level2
      comment.setLevel(2);
      comment.setParentId(parentId);
      comment.setToUid(toUserId);
    }

    commentMapper.insert(comment);

    threadService.updateCommentCount(articleMapper, article);
    return Result.success(comment);
  }

  @Override
  @Transactional
  public Result deleteCommentById(Long commentId) {
    Comment comment = commentMapper.selectById(commentId);

    if (comment == null) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }

    if (!comment.getAuthorId().equals(SysUserThreadLocal.get().getId())) {
      return Result.failure(ErrorCode.NO_LOGIN.getCode(),
          ErrorCode.NO_LOGIN.getMsg());
    }

    commentMapper.deleteById(comment);

    if (comment.getLevel() == 2) {
      return Result.success("Success");
    }

    if (comment.getLevel() == 1) {
      LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
      List<Comment> childComments = commentMapper.selectList(
          queryWrapper.eq(Comment::getParentId, commentId));

      // logically delete all child comments
      List<Long> commentIds = new ArrayList<>();
      childComments.forEach(child -> {
        child.setDeleted(true);
        commentIds.add(child.getId());
      });

      // only delete child comments if they exist
      if (commentIds.size() > 0) {
        commentMapper.deleteChildComments(commentIds);
      }

      return Result.success("Success");
    }

    return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(),
        ErrorCode.SYSTEM_ERROR.getMsg());
  }

  @Override
  public Boolean deleteArticleComments(Long articleId) {
    try {
      commentMapper.deleteArticleComments(articleId);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

}
