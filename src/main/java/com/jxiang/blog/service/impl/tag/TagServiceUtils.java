package com.jxiang.blog.service.impl.tag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jxiang.blog.pojo.Tag;
import com.jxiang.blog.vo.TagVo;

@Service
public class TagServiceUtils {

  TagVo copy(final Tag tag) {
    final TagVo tagVo = TagVo.builder()
        .id(String.valueOf(tag.getId()))
        .build();
    // copy properties of tag to tagVo, set field of tagVo to null if not in tag
    BeanUtils.copyProperties(tag, tagVo);
    return tagVo;
  }

  List<TagVo> copyList(final List<Tag> tagList) {
    final List<TagVo> tagVoList = new ArrayList<>();
    for (final Tag tag : tagList) {
      tagVoList.add(copy(tag));
    }
    return tagVoList;
  }

}
