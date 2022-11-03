package com.jxiang.blog.service;

import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.param.CategoryParam;
import com.jxiang.blog.vo.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface CategoryService {

  /**
   * retrieve category object given categoryId
   *
   * @param categoryId category id
   * @return Result
   */
  CategoryVo findCategoryById(Long categoryId);

  /**
   * find all categories
   *
   * @return Result
   */
  Result findAll();

  /**
   * create a category, admin only
   *
   * @param categoryParam object of categoryName and description
   * @param file          category image file
   * @return Result
   */
  Result createCategory(CategoryParam categoryParam, MultipartFile file);

  /**
   * retrieve a single category's detail by id
   *
   * @param id id of the category to retrieve
   * @return Result
   */
  Result getCategoryDetailById(Long id);

  /**
   * delete a category, given it's id
   *
   * @param id id of the category
   * @return Result
   */
  Result deleteCategoryById(String id);
}
