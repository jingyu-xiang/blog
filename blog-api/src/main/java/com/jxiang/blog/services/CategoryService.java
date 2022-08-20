package com.jxiang.blog.services;

import com.jxiang.blog.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

}
