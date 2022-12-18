package com.jxiang.blog.service.impl.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.CommentMapper;
import com.jxiang.blog.pojo.Comment;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.blog.vo.AuthorVo;
import com.jxiang.blog.vo.CommentVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceUtils {

  private final SysUserService sysUserService;
  private final CommentMapper commentMapper;

  @Autowired
  public CommentServiceUtils(SysUserService sysUserService, CommentMapper commentMapper) {
    this.sysUserService = sysUserService;
    this.commentMapper = commentMapper;
  }

  List<CommentVo> copyList(List<Comment> comments) {
    List<CommentVo> commentVoList = new ArrayList<>();

    for (Comment comment : comments) {
      commentVoList.add(copy(comment));
    }

    return commentVoList;
  }

  CommentVo copy(Comment comment) {
    CommentVo commentVo = new CommentVo();
    commentVo.setId(String.valueOf(comment.getId()));

    // copy fields of comment to commentVo if match (id, content, level)
    BeanUtils.copyProperties(comment, commentVo);

    // author info
    Long authorId = comment.getAuthorId();
    AuthorVo authorVo = sysUserService.findAuthorVoById(authorId);
    commentVo.setAuthor(authorVo);
    commentVo.setCreateDate(
        new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

    Integer level = comment.getLevel();
    // leve 1 comments have child comments
    if (level == 1) {
      List<CommentVo> commentVoList = findChildCommentVosByParentId(
          comment.getId());
      commentVo.setChildren(commentVoList);
    }

    // level 2 comments do not have child comments， but have a to-sysUser id to indicate who created parent comment
    if (level == 2) {
      Long toUid = comment.getToUid();
      Long toCommentId = comment.getToCommentId();

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
        CommentVo toCommentVo = copy(commentMapper.selectById(toCommentId));
        commentVo.setToComment(toCommentVo);
      }
    }

    return commentVo;
  }

  List<CommentVo> findChildCommentVosByParentId(Long id) {
    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

    queryWrapper
        .eq(Comment::getParentId, id)
        .eq(Comment::getLevel, 2);

    List<Comment> childComments = commentMapper.selectList(queryWrapper);
    return copyList(childComments);
  }

}
