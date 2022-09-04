package com.jxiang.blog.controllers;

import com.jxiang.blog.services.CategoryService;
import com.jxiang.blog.vo.params.CategoryParam;
import com.jxiang.blog.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public Result getCategories() {
        return categoryService.findAll();
    }

    @PostMapping("create")
    public Result createCategory(@RequestPart CategoryParam categoryParam, @RequestPart MultipartFile file) {
        return categoryService.createCategory(categoryParam, file);
    }

}
