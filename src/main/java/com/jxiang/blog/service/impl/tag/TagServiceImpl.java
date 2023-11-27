package com.jxiang.blog.service.impl.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.aop.admin.AdminOnly;
import com.jxiang.blog.aop.cache.MySpringCache;
import com.jxiang.blog.dao.mapper.TagMapper;
import com.jxiang.blog.pojo.Tag;
import com.jxiang.blog.service.TagService;
import com.jxiang.blog.util.QiniuUtils;
import com.jxiang.blog.vo.TagVo;
import com.jxiang.blog.vo.param.LimitParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {

  private final TagMapper tagMapper;

  private final QiniuUtils qiniuUtils;

  private final TagServiceUtils tagServiceUtils;

  public TagServiceImpl(
      final TagMapper tagMapper,
      final QiniuUtils qiniuUtils,
      final TagServiceUtils tagServiceUtils) {
    this.tagMapper = tagMapper;
    this.qiniuUtils = qiniuUtils;
    this.tagServiceUtils = tagServiceUtils;
  }

  @Override
  @MySpringCache(name = "listHotTags")
  public Result listHotTags(final LimitParam limitParam) {
    final List<Long> tagIdList = tagMapper.findHotTagIds(limitParam.getLimit());

    if (CollectionUtils.isEmpty(tagIdList)) {
      return Result.success(Collections.emptyList());
    }

    final List<Tag> tagList = tagMapper.selectBatchIds(tagIdList);

    return Result.success(tagServiceUtils.copyList(tagList));
  }

  @Override
  @MySpringCache(name = "getAllTags")
  public Result getAllTags() {
    final List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
    return Result.success(tagServiceUtils.copyList(tags));
  }

  @Override
  public List<TagVo> findTagsByArticleId(final Long articleId) {
    final List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
    return tagServiceUtils.copyList(tags);
  }

  @Override
  @Transactional
  @AdminOnly
  public Result createTag(final String tagName, final MultipartFile file) {
    // check repeat
    final LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Tag::getTagName, tagName).last("LIMIT 1");
    final List<Tag> tags = tagMapper.selectList(queryWrapper);
    if (tags.size() >= 1) {
      return Result.failure(
          ErrorCode.ITEM_ALREADY_EXISTS.getCode(),
          ErrorCode.ITEM_ALREADY_EXISTS.getMsg());
    }

    final Tag tag = new Tag();
    tag.setTagName(tagName);
    tagMapper.insert(tag);

    final Long tagId = tag.getId();

    final String originalFilename = file.getOriginalFilename();
    final String fileNameToUpload = "tags/" + tagId.toString() + "/" + originalFilename;
    tag.setAvatar(fileNameToUpload);

    final Map<String, Object> uploaded = qiniuUtils.upload(file, fileNameToUpload);

    if ((boolean) uploaded.get("success")) {
      tag.setAvatar(uploaded.get("urn") + "/" + fileNameToUpload);
      tagMapper.updateById(tag);
      return Result.success(tagServiceUtils.copy(tag));
    }

    return Result.failure(
        ErrorCode.FILE_UPLOAD_FAILURE.getCode(),
        ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
  }

  @Override
  public Result findTagVoById(final Long id) {
    try {
      final Tag tag = tagMapper.selectById(id);
      return Result.success(tag);
    } catch (final Exception e) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }
  }

  @Override
  @AdminOnly
  public Result deleteTagById(final String id) {
    final Long tagId = Long.parseLong(id);
    final Tag tag = tagMapper.selectById(tagId);

    if (tag != null) {
      final int success = tagMapper.deleteById(tagId);

      return success == 1
          ? Result.success(id)
          : Result.failure(
              ErrorCode.SYSTEM_ERROR.getCode(),
              ErrorCode.SYSTEM_ERROR.getMsg());
    }

    return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
  }

}
