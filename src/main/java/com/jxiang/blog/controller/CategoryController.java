package com.jxiang.blog.controller;

import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.vo.param.CategoryParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public Result getCategories() {
    return categoryService.findAll();
  }

  @PostMapping("create")
  public Result createCategory(
      @RequestPart CategoryParam categoryParam,
      @RequestPart MultipartFile file
  ) {
    if (file.isEmpty()) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

    return categoryService.createCategory(categoryParam, file);
  }

  @GetMapping("{id}")
  public Result getCategoryDetailById(@PathVariable Long id) {
    return categoryService.getCategoryDetailById(id);
  }

  @DeleteMapping("/delete/{id}")
  public Result deleteCategoryById(@PathVariable String id) {
    return categoryService.deleteCategoryById(id);
  }

}
