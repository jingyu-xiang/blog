package com.jxiang.blog.service.impl.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jxiang.blog.pojo.Category;
import com.jxiang.blog.vo.CategoryVo;

@Service
public class CategoryServiceUtils {

  CategoryVo copy(final Category category) {
    final CategoryVo categoryVo = CategoryVo.builder()
        .id(String.valueOf(category.getId()))
        .build();
    BeanUtils.copyProperties(category, categoryVo);
    return categoryVo;
  }

  List<CategoryVo> copyList(final List<Category> categoryList) {
    final List<CategoryVo> categoryVoList = new ArrayList<>();
    for (final Category category : categoryList) {
      categoryVoList.add(copy(category));
    }
    return categoryVoList;
  }

}
