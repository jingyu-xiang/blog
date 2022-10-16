package com.jxiang.blog.service.impl.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jxiang.blog.aop.cache.MySpringCache;
import com.jxiang.blog.dao.mapper.CategoryMapper;
import com.jxiang.blog.pojo.Category;
import com.jxiang.blog.service.CategoryService;
import com.jxiang.blog.util.beans.QiniuUtils;
import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.params.CategoryParam;
import com.jxiang.blog.vo.results.ErrorCode;
import com.jxiang.blog.vo.results.Result;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CategoryMapper categoryMapper;
  @Autowired
  private QiniuUtils qiniuUtils;
  @Autowired
  private CategoryServiceUtils categoryServiceUtils;

  @Override
  public CategoryVo findCategoryById(Long categoryId) {
    Category category = categoryMapper.selectById(categoryId);
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
    try {
      Category category = categoryMapper.selectById(id);
      return Result.success(categoryServiceUtils.copy(category));
    } catch (Exception e) {
      return Result.failure(ErrorCode.NOT_FOUND.getCode(),
          ErrorCode.NOT_FOUND.name());
    }
  }


}
