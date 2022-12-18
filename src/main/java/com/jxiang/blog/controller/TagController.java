package com.jxiang.blog.controller;

import com.jxiang.blog.service.TagService;
import com.jxiang.blog.vo.param.LimitParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/tags")
public class TagController {

  @Autowired
  TagService tagService;

  @PostMapping("create")
  public Result createTag(@RequestPart String tagName,
                          @RequestPart MultipartFile file) {
    if (file.isEmpty()) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

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

  @DeleteMapping("delete/{id}")
  public Result deleteTagById(@PathVariable("id") String id) {
    return tagService.deleteTagById(id);
  }

}
