package com.jxiang.blog.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.vo.param.CategoryParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/categories")
@RequiredArgsConstructor
public class CategoryController {

  private CategoryService categoryService;

  @GetMapping
  public Result getCategories() {
    return categoryService.findAll();
  }

  @PostMapping("create")
  public Result createCategory(
      @RequestPart final CategoryParam categoryParam,
      @RequestPart final MultipartFile file) {
    if (file.isEmpty()) {
      return Result.failure(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
    }

    return categoryService.createCategory(categoryParam, file);
  }

  @GetMapping("{id}")
  public Result getCategoryDetailById(@PathVariable final Long id) {
    return categoryService.getCategoryDetailById(id);
  }

  @DeleteMapping("/delete/{id}")
  public Result deleteCategoryById(@PathVariable final String id) {
    return categoryService.deleteCategoryById(id);
  }

}
