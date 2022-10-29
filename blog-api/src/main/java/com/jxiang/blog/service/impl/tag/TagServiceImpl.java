package com.jxiang.blog.service.impl.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.aop.admin.AdminOnly;
import com.jxiang.blog.aop.cache.MySpringCache;
import com.jxiang.blog.dao.mapper.TagMapper;
import com.jxiang.blog.service.TagService;
import com.jxiang.blog.util.beans.QiniuUtils;
import com.jxiang.common.pojo.Tag;
import com.jxiang.common.vo.TagVo;
import com.jxiang.common.vo.param.LimitParam;
import com.jxiang.common.vo.result.ErrorCode;
import com.jxiang.common.vo.result.Result;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TagServiceImpl implements TagService {

  private final TagMapper tagMapper;
  private final QiniuUtils qiniuUtils;
  private final TagServiceUtils tagServiceUtils;

  @Autowired
  public TagServiceImpl(
      TagMapper tagMapper,
      QiniuUtils qiniuUtils,
      TagServiceUtils tagServiceUtils
  ) {
    this.tagMapper = tagMapper;
    this.qiniuUtils = qiniuUtils;
    this.tagServiceUtils = tagServiceUtils;
  }

  @Override
  @MySpringCache(name = "listHotTags")
  public Result listHotTags(LimitParam limitParam) {
    List<Long> tagIdList = tagMapper.findHotTagIds(limitParam.getLimit());

    if (CollectionUtils.isEmpty(tagIdList)) {
      return Result.success(Collections.emptyList());
    }

    final List<Tag> tagList = tagMapper.selectBatchIds(tagIdList);

    return Result.success(tagList);
  }

  @Override
  public List<TagVo> findTagsByArticleId(Long articleId) {
    List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
    return tagServiceUtils.copyList(tags);
  }

  @Override
  @MySpringCache(name = "getAllTags")
  public Result getAllTags() {
    List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
    return Result.success(tagServiceUtils.copyList(tags));
  }

  @Override
  @Transactional
  @AdminOnly
  public Result createTag(String tagName, MultipartFile file) {
    // check repeat
    LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Tag::getTagName, tagName).last("LIMIT 1");
    List<Tag> tags = tagMapper.selectList(queryWrapper);
    if (tags.size() >= 1) {
      return Result.failure(ErrorCode.ITEM_ALREADY_EXISTS.getCode(),
          ErrorCode.ITEM_ALREADY_EXISTS.getMsg());
    }

    Tag tag = new Tag();
    tag.setTagName(tagName);
    tagMapper.insert(tag);

    Long tagId = tag.getId();

    String originalFilename = file.getOriginalFilename();
    String fileNameToUpload =
        "tags/" + tagId.toString() + "/" + originalFilename;
    tag.setAvatar(fileNameToUpload);

    Map<String, Object> uploaded = qiniuUtils.upload(file, fileNameToUpload);

    if ((boolean) uploaded.get("success")) {
      tag.setAvatar(uploaded.get("urn") + "/" + fileNameToUpload);
      tagMapper.updateById(tag);
      return Result.success(tagServiceUtils.copy(tag));
    }

    return Result.failure(ErrorCode.FILE_UPLOAD_FAILURE.getCode(),
        ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
  }

  @Override
  public Result findTagVoById(Long id) {
    try {
      Tag tag = tagMapper.selectById(id);
      return Result.success(tag);
    } catch (Exception e) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.getMsg());
    }
  }

  @Override
  @AdminOnly
  public Result deleteTagById(String id) {
    Long tagId = Long.parseLong(id);
    Tag tag = tagMapper.selectById(tagId);

    if (tag != null) {
      int success = tagMapper.deleteById(tagId);

      return success == 1
          ? Result.success(id)
          : Result.failure(ErrorCode.SYSTEM_ERROR.getCode(),
              ErrorCode.SYSTEM_ERROR.getMsg());
    }

    return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
  }
}
