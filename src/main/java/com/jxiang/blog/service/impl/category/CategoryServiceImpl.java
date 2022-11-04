package com.jxiang.blog.service.impl.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.aop.admin.AdminOnly;
import com.jxiang.blog.aop.cache.MySpringCache;
import com.jxiang.blog.dao.mapper.CategoryMapper;
import com.jxiang.blog.pojo.Category;
import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.util.beans.QiniuUtils;
import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.param.CategoryParam;
import com.jxiang.blog.vo.result.ErrorCode;
import com.jxiang.blog.vo.result.Result;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryMapper categoryMapper;
  private final QiniuUtils qiniuUtils;
  private final CategoryServiceUtils categoryServiceUtils;

  @Autowired
  public CategoryServiceImpl(
      CategoryMapper categoryMapper,
      QiniuUtils qiniuUtils,
      CategoryServiceUtils categoryServiceUtils
  ) {
    this.categoryMapper = categoryMapper;
    this.qiniuUtils = qiniuUtils;
    this.categoryServiceUtils = categoryServiceUtils;
  }

  @Override
  public CategoryVo findCategoryById(Long categoryId) {
    Category category = categoryMapper.selectById(categoryId);

    if (category == null) {
      category = new Category();
      category.setId(-1L);
      category.setAvatar(
          "https://www.publicdomainpictures.net/pictures/280000/velka/not-found-image-15383864787lu.jpg"
      );
      category.setCategoryName("NOT FOUND");
      category.setDescription("Category might be deleted or never exist");
    }

    return categoryServiceUtils.copy(category);
  }

  @Override
  @MySpringCache(name = "findAll")
  public Result findAll() {
    List<Category> categories = categoryMapper.selectList(
        new LambdaQueryWrapper<>());
    return Result.success(categoryServiceUtils.copyList(categories));
  }

  @Override
  @Transactional
  @AdminOnly
  public Result createCategory(CategoryParam categoryParam,
      MultipartFile file) {
    String categoryName = categoryParam.getCategoryName();
    String description = categoryParam.getDescription();

    // check repeat
    LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(Category::getCategoryName, categoryName).last("LIMIT 1");

    List<Category> categories = categoryMapper.selectList(queryWrapper);
    if (categories.size() >= 1) {
      return Result.failure(ErrorCode.ITEM_ALREADY_EXISTS.getCode(),
          ErrorCode.ITEM_ALREADY_EXISTS.getMsg());
    }

    Category category = new Category();
    category.setCategoryName(categoryName);
    category.setDescription(description);
    categoryMapper.insert(category);

    Long categoryId = category.getId();

    String originalFilename = file.getOriginalFilename();

    System.out.println(originalFilename);
    String fileNameToUpload =
        "categories/" + categoryId.toString() + "/" + originalFilename;
    category.setAvatar(fileNameToUpload);

    Map<String, Object> uploaded = qiniuUtils.upload(file, fileNameToUpload);

    if ((boolean) uploaded.get("success")) {
      category.setAvatar(uploaded.get("urn") + "/" + fileNameToUpload);
      categoryMapper.updateById(category);
      return Result.success(categoryServiceUtils.copy(category));
    }

    return Result.failure(ErrorCode.FILE_UPLOAD_FAILURE.getCode(),
        ErrorCode.FILE_UPLOAD_FAILURE.getMsg());
  }

  @Override
  public Result getCategoryDetailById(Long id) {
    Category category = categoryMapper.selectById(id);
    if (category == null) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.name());
    }
    return Result.success(categoryServiceUtils.copy(category));
  }

  @Override
  @AdminOnly
  public Result deleteCategoryById(String id) {
    Long categoryId = Long.parseLong(id);

    Category categoryToDelete = categoryMapper.selectById(categoryId);

    if (categoryToDelete != null) {
      int success = categoryMapper.deleteById(categoryId);
      return success == 1
          ? Result.success(id)
          : Result.failure(ErrorCode.SYSTEM_ERROR.getCode(),
              ErrorCode.SYSTEM_ERROR.getMsg());
    }

    return Result.failure(ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMsg());
  }

}
