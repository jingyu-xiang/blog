package com.jxiang.blog.service.impl.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.dao.mapper.TagMapper;
import com.jxiang.blog.pojo.Tag;
import com.jxiang.blog.service.TagService;
import com.jxiang.blog.util.beans.QiniuUtils;
import com.jxiang.blog.vo.TagVo;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TagServiceImpl implements TagService {

  @Autowired
  private TagMapper tagMapper;
  @Autowired
  private QiniuUtils qiniuUtils;
  @Autowired
  private TagServiceUtil tagServiceUtil;

  @Override
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
    return tagServiceUtil.copyList(tags);
  }

  @Override
  public Result getAllTags() {
    List<Tag> tags = tagMapper.selectList(new LambdaQueryWrapper<>());
    return Result.success(tagServiceUtil.copyList(tags));
  }

  @Override
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
      return Result.success(tagServiceUtil.copy(tag));
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
}
