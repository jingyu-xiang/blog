package com.jxiang.blog.services;

import com.jxiang.blog.vo.CategoryVo;

public interface CategoryService {

    /**
     * retrieve category object given categoryId
     *
     * @param categoryId category id
     * @return Result
     */
    CategoryVo findCategoryById(Long categoryId);

}
