package com.jxiang.blog.controllers;

import com.jxiang.blog.services.TagService;
import com.jxiang.blog.vo.params.LimitParam;
import com.jxiang.blog.vo.params.TagParam;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/tags")
public class TagController {

    @Autowired
    TagService tagService;

    @PostMapping("create")
    public Result createTag(@RequestPart String tagName, @RequestPart MultipartFile file) {
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

}
