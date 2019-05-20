package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface CategoryService {
    Category saveCategory(Category category);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    Set<Category> saveCategories(Set<Category> categories);

    Category updateCategoryByName(Category category);

    void deleteCategoryById(Long id);
}
