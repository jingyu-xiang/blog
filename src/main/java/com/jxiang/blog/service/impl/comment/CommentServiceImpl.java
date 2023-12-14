package com.jxiang.blog.service.impl.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.CommentMapper;
import com.jxiang.blog.pojo.Article;
import com.jxiang.blog.pojo.Comment;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.service.CommentService;
import com.jxiang.blog.service.thread.ThreadService;
import com.jxiang.blog.util.SysUserThreadLocal;
import com.jxiang.blog.vo.CommentVo;
import com.jxiang.blog.vo.param.CommentParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentMapper commentMapper;

  private final ArticleMapper articleMapper;

  private final ThreadService threadService;

  private final CommentServiceUtils commentServiceUtils;

  @Override
  public Result getCommentsByArticleId(final String id) {
    final LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper // parent comments
        .eq(Comment::getArticleId, id)
        .eq(Comment::getLevel, 1);

    final List<Comment> comments = commentMapper.selectList(queryWrapper);

    final List<CommentVo> commentVoList = commentServiceUtils.copyList(comments);

    return Result.success(commentVoList);
  }

  @Override
  public Result createComment(final CommentParam commentParam) {

    if (commentParam.getArticleId() == null) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }

    Article article;

    try {
      article = articleMapper.selectById(commentParam.getArticleId());
    } catch (final Exception e) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }

    final SysUser author = SysUserThreadLocal.get();
    final Comment comment = Comment.builder()
        .articleId(Long.valueOf(commentParam.getArticleId()))
        .authorId(author.getId())
        .content(commentParam.getContent())
        .createDate(System.currentTimeMillis())
        .build();

    final long parentId = commentParam.getParent() == null
        ? -1L
        : Long.parseLong(commentParam.getParent());
    final long toUserId = commentParam.getToUserId() == null
        ? -1L
        : Long.parseLong(commentParam.getToUserId());
    final long toCommentId = commentParam.getToCommentId() == null
        ? -1L
        : Long.parseLong(commentParam.getToCommentId());

    if (parentId == -1L) {
      // level1
      comment.setLevel(1);
    } else {
      // level2
      comment.setLevel(2);
    }

    comment.setToCommentId(toCommentId);
    comment.setParentId(parentId);
    comment.setToUid(toUserId);

    commentMapper.insert(comment);

    threadService.updateCommentCount(articleMapper, article);
    return Result.success(comment);
  }

  @Override
  @Transactional
  public Result deleteCommentById(final String id) {
    final long commentId = Long.parseLong(id);
    final Comment comment = commentMapper.selectById(commentId);

    if (comment == null) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
    }

    if (!comment.getAuthorId().equals(SysUserThreadLocal.get().getId())) {
      return Result.failure(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
    }

    final int success = commentMapper.deleteById(comment);

    if (success == 1) {
      if (comment.getLevel() == 2) {
        threadService.updateCommentCount(articleMapper, comment.getArticleId(), false, 1);
        return Result.success("Success");
      }

      if (comment.getLevel() == 1) {
        final LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        final List<Comment> childComments = commentMapper.selectList(
            queryWrapper.eq(Comment::getParentId, commentId));

        // logically delete all child comments
        final List<Long> childIds = new ArrayList<>();
        int count = 1;

        for (final Comment child : childComments) {
          count += 1;
          child.setDeleted(true);
          childIds.add(child.getId());
        }

        // only delete child comments if they exist
        if (childIds.size() > 0) {
          commentMapper.deleteChildComments(childIds);
        }

        threadService.updateCommentCount(articleMapper, comment.getArticleId(), false, count);

        return Result.success("Success");
      }
    }

    return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
  }

  @Override
  @Transactional
  public Boolean deleteArticleComments(final Long articleId) {
    try {
      // delete comment counts
      final int count = commentMapper
          .selectList(new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, articleId))
          .size();

      if (count > 0) {
        commentMapper.deleteArticleComments(articleId);
        threadService.updateCommentCount(articleMapper, articleId, false, count);
      }
    } catch (final Exception e) {
      return false;
    }
    return true;
  }

}
