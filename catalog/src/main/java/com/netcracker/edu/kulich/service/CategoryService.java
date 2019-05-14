package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Category;

import java.util.Set;

public interface CategoryService {
    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    Set<Category> saveCategories(Set<Category> categories);

    Category updateCategory(Category category);

    void deleteCategoryById(Long id);
}
