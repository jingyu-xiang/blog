package com.jxiang.blog.service.impl.category;

import com.jxiang.blog.pojo.Category;
import com.jxiang.blog.vo.CategoryVo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceUtils {

  CategoryVo copy(final Category category) {
    final CategoryVo categoryVo = new CategoryVo();
    categoryVo.setId(String.valueOf(category.getId()));
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
