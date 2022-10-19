package com.jxiang.blog.controller;

import com.jxiang.blog.service.CategoryService;
import com.jxiang.common.vo.params.CategoryParam;
import com.jxiang.common.vo.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
  public Result createCategory(
      @RequestPart CategoryParam categoryParam,
      @RequestPart MultipartFile file
  ) {
    return categoryService.createCategory(categoryParam, file);
  }

  @GetMapping("{id}")
  public Result getCategoryDetailById(@PathVariable Long id) {
    return categoryService.getCategoryDetailById(id);
  }

}
