package com.jxiang.blog.services.impl;

import com.jxiang.blog.dao.TagMapper;
import com.jxiang.blog.pojo.Tag;
import com.jxiang.blog.services.TagService;
import com.jxiang.blog.vo.Result;
import com.jxiang.blog.vo.TagVo;
import com.jxiang.blog.vo.params.LimitParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

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
        return copyList(tags);
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        // copy properties of tag to tagVo, set field of tagVo to null if it is not in tag
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

}
