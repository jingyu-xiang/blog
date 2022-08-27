package com.jxiang.blog.services;

import com.jxiang.blog.vo.CategoryVo;
import com.jxiang.blog.vo.results.Result;

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

}
