package com.jxiang.blog.service.impl.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.aop.admin.AdminOnly;
import com.jxiang.blog.aop.cache.MySpringCache;
import com.jxiang.blog.dao.mapper.CategoryMapper;
import com.jxiang.blog.pojo.Category;
import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.util.QiniuUtils;
import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.param.CategoryParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryMapper categoryMapper;

  private final QiniuUtils qiniuUtils;

  private final CategoryServiceUtils categoryServiceUtils;

  public CategoryServiceImpl(
      final CategoryMapper categoryMapper,
      final QiniuUtils qiniuUtils,
      final CategoryServiceUtils categoryServiceUtils) {
    this.categoryMapper = categoryMapper;
    this.qiniuUtils = qiniuUtils;
    this.categoryServiceUtils = categoryServiceUtils;
  }

  @Override
  public CategoryVo findCategoryById(final Long categoryId) {
    Category category = categoryMapper.selectById(categoryId);

    if (category == null) {
      category = new Category();
      category.setId(-1L);
      category.setAvatar(
          "https://www.publicdomainpictures"
              + ".net/pictures/280000/velka/not-found-image-15383864787lu.jpg");
      category.setCategoryName("NOT FOUND");
      category.setDescription("Category might be deleted or never exist");
    }

    return categoryServiceUtils.copy(category);
  }

  @Override
  @MySpringCache(name = "findAll")
  public Result findAll() {
    final List<Category> categories = categoryMapper.selectList(
        new LambdaQueryWrapper<>());
    return Result.success(categoryServiceUtils.copyList(categories));
  }

  @Override
  @Transactional
  @AdminOnly
  public Result createCategory(
      final CategoryParam categoryParam,
      final MultipartFile file) {
    final String categoryName = categoryParam.getCategoryName();
    final String description = categoryParam.getDescription();

    // check repeat
    final LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Category::getCategoryName, categoryName).last("LIMIT 1");

    final List<Category> categories = categoryMapper.selectList(queryWrapper);
    if (categories.size() >= 1) {
      return Result.failure(
          ErrorCode.ITEM_ALREADY_EXISTS.getCode(),
          ErrorCode.ITEM_ALREADY_EXISTS.getMsg());
    }

    final Category category = new Category();
    category.setCategoryName(categoryName);
    category.setDescription(description);
    categoryMapper.insert(category);

    final Long categoryId = category.getId();

    final String originalFilename = file.getOriginalFilename();

    System.out.println(originalFilename);
    final String fileNameToUpload = "categories/" + categoryId.toString() + "/" + originalFilename;
    category.setAvatar(fileNameToUpload);

    final Map<String, Object> uploaded = qiniuUtils.upload(file, fileNameToUpload);

    if ((boolean) uploaded.get("success")) {
      category.setAvatar(uploaded.get("urn") + "/" + fileNameToUpload);
      categoryMapper.updateById(category);
      return Result.success(categoryServiceUtils.copy(category));
    }

    return Result.failure(
        ErrorCode.FILE_UPLOAD_FAILURE.getCode(),
        ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
  }

  @Override
  public Result getCategoryDetailById(final Long id) {
    final Category category = categoryMapper.selectById(id);
    if (category == null) {
      return Result.failure(
          ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.name());
    }
    return Result.success(categoryServiceUtils.copy(category));
  }

  @Override
  @AdminOnly
  public Result deleteCategoryById(final String id) {
    final Long categoryId = Long.parseLong(id);

    final Category categoryToDelete = categoryMapper.selectById(categoryId);

    if (categoryToDelete != null) {
      final int success = categoryMapper.deleteById(categoryId);
      return success == 1
          ? Result.success(id)
          : Result.failure(
              ErrorCode.SYSTEM_ERROR.getCode(),
              ErrorCode.SYSTEM_ERROR.getMsg());
    }

    return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
  }

}
