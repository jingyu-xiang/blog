package com.jxiang.blog.controller;

import com.jxiang.blog.service.TagService;
import com.jxiang.common.vo.param.LimitParam;
import com.jxiang.common.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/tags")
public class TagController {

  @Autowired
  TagService tagService;

  @PostMapping("create")
  public Result createTag(@RequestPart String tagName,
      @RequestPart MultipartFile file) {
    return tagService.createTag(tagName, file);
  }

  @PostMapping("hots")
  public Result retrieveMostPopularTags(@RequestBody LimitParam limitParam) {
    return tagService.listHotTags(limitParam);
  }

  @GetMapping
  public Result getAllTags() {
    return tagService.getAllTags();
  }

  @GetMapping("{id}")
  public Result findTagDetailById(@PathVariable("id") Long id) {
    return tagService.findTagVoById(id);
  }

}
