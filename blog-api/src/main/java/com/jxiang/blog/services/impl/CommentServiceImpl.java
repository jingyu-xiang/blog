package com.jxiang.blog.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.ArticleMapper;
import com.jxiang.blog.dao.mapper.CommentMapper;
import com.jxiang.blog.dao.mapper.SysUserMapper;
import com.jxiang.blog.pojo.Comment;
import com.jxiang.blog.pojo.SysUser;
import com.jxiang.blog.services.CommentService;
import com.jxiang.blog.services.SysUserService;
import com.jxiang.blog.utils.SysUserThreadLocal;
import com.jxiang.blog.vo.CommentVo;
import com.jxiang.blog.vo.SysUserVo;
import com.jxiang.blog.vo.params.CommentParam;
import com.jxiang.blog.vo.results.ErrorCode;
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
    ArticleMapper articleMapper;

    @Autowired
    SysUserMapper sysUserMapper;

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

    @Override
    public Result createComment(CommentParam commentParam) {
        if (commentParam.getArticleId() == null ||
            articleMapper.selectById(commentParam.getArticleId()) == null
        ) {
            return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
        }

        SysUser sysUser = SysUserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());

        Long parentId = commentParam.getParent();
        Long toUserId = commentParam.getToUserId();
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

        return Result.success(comment);
    }

    @Override
    public Result deleteCommentById(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);

        if (comment == null) {
            return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
        }

        comment.setDeleted(true);
        commentMapper.updateById(comment);

        if (comment.getLevel() == 2) {
            return Result.success(comment);
        }

        if (comment.getLevel() == 1) {
            LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
            List<Comment> childComments = commentMapper
                .selectList(
                    queryWrapper.eq(Comment::getParentId, commentId)
                );

            // logically delete all child comments
            List<Long> commentIds = new ArrayList<>();
            childComments.forEach(cmt -> {
                cmt.setDeleted(true);
                commentIds.add(cmt.getId());
            });

            // only delete child comments if they exist
            if (commentIds.size() > 0) {
                commentMapper.deleteChildComments(commentIds);
            }

            return Result.success(childComments);
        }

        return Result.failure(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMsg());
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
        // leve 1 comments have child comments
        if (level == 1) {
            List<CommentVo> commentVoList = findChildCommentVosByParentId(comment.getId());
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

    private List<CommentVo> findChildCommentVosByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper
            .eq(Comment::getParentId, id)
            .eq(Comment::getLevel, 2);

        List<Comment> childComments = commentMapper.selectList(queryWrapper);
        return copyList(childComments);
    }

}
