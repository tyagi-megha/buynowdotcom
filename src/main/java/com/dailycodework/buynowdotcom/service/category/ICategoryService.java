package com.dailycodework.buynowdotcom.service.category;

import com.dailycodework.buynowdotcom.model.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(Category category);
    Category updateCategory(Category category, Long categoryId);
    void deleteCategory(Long categoryId);
    List<Category> getAllCategories();
    Category findCategoryById(Long categoryId);
    Category findCategoryByName(String name);

}
