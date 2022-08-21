package com.jxiang.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.CommentMapper;
import com.jxiang.blog.pojo.Comment;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.services.CommentService;
import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.vo.CommentVo;
import com.jxiang.blog.vo.SysUserVo;
import com.jxiang.blog.vo.results.Result;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result getCommentsByArticleId(String id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper // parent comments
            .eq(Comment::getArticleId, id)
            .eq(Comment::getLevel, 1);

        List<Comment> comments = commentMapper.selectList(queryWrapper);

        List<CommentVo> commentVoList = copyList(comments);

        return Result.success(commentVoList);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();

        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }

        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();

        // copy fields of comment to commentVo if match (id, content, level)
        BeanUtils.copyProperties(comment, commentVo);

        // author info
        Long authorId = comment.getAuthorId();
        SysUserVo sysUserVo = sysUserService.getSysUserVoById(authorId);
        commentVo.setAuthor(sysUserVo);
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        Integer level = comment.getLevel();
        if (level == 1) { // leve 1 comments have child comments
            List<CommentVo> commentVoList = findChildCommentVosByParentId(comment.getId());
            commentVo.setChildren(commentVoList);
        }

        if (level == 2) { // level 2 comments do not have child comments have a to-sysUser id
            Long toUid = comment.getToUid();
            SysUserVo toSysUserVo = sysUserService.getSysUserVoById(toUid);
            commentVo.setToUser(toSysUserVo);
        }

        return commentVo;
    }

    private List<CommentVo> findChildCommentVosByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
            .eq(Comment::getParentId, id)
            .eq(Comment::getLevel, 2);

        List<Comment> childComments = commentMapper.selectList(queryWrapper);
        return copyList(childComments);
    }

}
