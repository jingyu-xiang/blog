package com.jxiang.blog.controllers;

import com.jxiang.blog.services.TagService;
import com.jxiang.blog.vo.Result;
import com.jxiang.blog.vo.params.LimitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tags")
public class TagController {

    @Autowired
    TagService tagService;

    @PostMapping("hots")
    public Result retrieveMostPopularTags(@RequestBody LimitParam limitParam) {
        return tagService.listHotTags(limitParam);
    }

}
