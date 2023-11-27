package com.jxiang.blog.controller;

import com.jxiang.blog.service.TagService;
import com.jxiang.blog.vo.param.LimitParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  private TagService tagService;

  @PostMapping("create")
  public Result createTag(
      @RequestPart final String tagName,
      @RequestPart final MultipartFile file) {
    if (file.isEmpty()) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

    return tagService.createTag(tagName, file);
  }

  @PostMapping("hots")
  public Result retrieveMostPopularTags(@RequestBody final LimitParam limitParam) {
    return tagService.listHotTags(limitParam);
  }

  @GetMapping
  public Result getAllTags() {
    return tagService.getAllTags();
  }

  @GetMapping("{id}")
  public Result findTagDetailById(@PathVariable("id") final Long id) {
    return tagService.findTagVoById(id);
  }

  @DeleteMapping("delete/{id}")
  public Result deleteTagById(@PathVariable("id") final String id) {
    return tagService.deleteTagById(id);
  }

}
