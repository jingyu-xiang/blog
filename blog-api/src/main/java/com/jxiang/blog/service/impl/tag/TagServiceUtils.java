package com.jxiang.blog.service.impl.tag;


import com.jxiang.common.pojo.Tag;
import com.jxiang.common.vo.TagVo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TagServiceUtils {

  TagVo copy(Tag tag) {
    TagVo tagVo = new TagVo();
    tagVo.setId(String.valueOf(tag.getId()));
    // copy properties of tag to tagVo, set field of tagVo to null if it is not in tag
    BeanUtils.copyProperties(tag, tagVo);
    return tagVo;
  }

  List<TagVo> copyList(List<Tag> tagList) {
    List<TagVo> tagVoList = new ArrayList<>();
    for (Tag tag : tagList) {
      tagVoList.add(copy(tag));
    }
    return tagVoList;
  }

}
