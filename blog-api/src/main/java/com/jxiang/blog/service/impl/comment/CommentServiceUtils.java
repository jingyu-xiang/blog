package com.jxiang.blog.service.impl.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.CommentMapper;
import com.jxiang.blog.service.SysUserService;
import com.jxiang.common.pojo.Comment;
import com.jxiang.common.vo.CommentVo;
import com.jxiang.common.vo.SysUserVo;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    SysUserVo sysUserVo = sysUserService.getSysUserVoById(authorId);
    commentVo.setAuthor(sysUserVo);
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
      SysUserVo toSysUserVo = sysUserService.getSysUserVoById(toUid);
      commentVo.setToUser(toSysUserVo);
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
