package com.jxiang.blog.service.impl.category;

import com.jxiang.blog.pojo.Category;
import com.jxiang.blog.vo.CategoryVo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceUtils {

  CategoryVo copy(Category category) {
    CategoryVo categoryVo = new CategoryVo();
    categoryVo.setId(String.valueOf(category.getId()));
    BeanUtils.copyProperties(category, categoryVo);
    return categoryVo;
  }

  List<CategoryVo> copyList(List<Category> categoryList) {
    List<CategoryVo> categoryVoList = new ArrayList<>();
    for (Category category : categoryList) {
      categoryVoList.add(copy(category));
    }
    return categoryVoList;
  }

}
