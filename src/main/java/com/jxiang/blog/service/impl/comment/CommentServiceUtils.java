package com.jxiang.blog.service.impl.comment;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.CommentMapper;
import com.jxiang.blog.pojo.Comment;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.vo.AuthorVo;
import com.jxiang.blog.vo.CommentVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceUtils {

  private final SysUserService sysUserService;
  private final CommentMapper commentMapper;

  List<CommentVo> copyList(final List<Comment> comments) {
    final List<CommentVo> commentVoList = new ArrayList<>();

    for (final Comment comment : comments) {
      commentVoList.add(copy(comment));
    }

    return commentVoList;
  }

  CommentVo copy(final Comment comment) {
    final CommentVo commentVo = new CommentVo();
    commentVo.setId(String.valueOf(comment.getId()));

    // copy fields of comment to commentVo if match (id, content, level)
    BeanUtils.copyProperties(comment, commentVo);

    // author info
    final Long authorId = comment.getAuthorId();
    final AuthorVo authorVo = sysUserService.findAuthorVoById(authorId);
    commentVo.setAuthor(authorVo);
    commentVo.setCreateDate(
        new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

    final Integer level = comment.getLevel();
    // leve 1 comments have child comments
    if (level == 1) {
      final List<CommentVo> commentVoList = findChildCommentVosByParentId(
          comment.getId());
      commentVo.setChildren(commentVoList);
    }

    // level 2 comments do not have child comments， but have a to-sysUser id to
    // indicate who
    // created parent comment
    if (level == 2) {
      final Long toUid = comment.getToUid();
      final Long toCommentId = comment.getToCommentId();

      if (toUid != -1L) {
        AuthorVo toAuthorVo = sysUserService.findAuthorVoById(toUid);
        if (toAuthorVo != null) {
          commentVo.setToUser(toAuthorVo);
        } else {
          toAuthorVo = new AuthorVo();
          toAuthorVo.setNickname("unknown");
          toAuthorVo.setGithub("unknown");
          commentVo.setToUser(toAuthorVo);
        }
      }

      if (toCommentId != -1L) {
        final CommentVo toCommentVo = copy(commentMapper.selectById(toCommentId));
        commentVo.setToComment(toCommentVo);
      }
    }

    return commentVo;
  }

  List<CommentVo> findChildCommentVosByParentId(final Long id) {
    final LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper
        .eq(Comment::getParentId, id)
        .eq(Comment::getLevel, 2);

    final List<Comment> childComments = commentMapper.selectList(queryWrapper);
    return copyList(childComments);
  }

}
