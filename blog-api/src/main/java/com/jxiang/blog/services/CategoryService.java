package com.jxiang.blog.services;

import com.jxiang.blog.vo.CategoryVo;

import java.util.List;

public interface CategoryService {

    /**
     * retrieve category object given categoryId
     *
     * @param categoryId category id
     * @return Result
     */
    CategoryVo findCategoryById(Long categoryId);

}
