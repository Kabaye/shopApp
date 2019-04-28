package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.service.exception.CategoryServiceException;

import java.util.Set;

public interface CategoryService {
    Category saveCategory(Category category) throws CategoryServiceException;

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    Set<Category> saveCategories(Set<Category> categories) throws CategoryServiceException;

    Category updateCategory(Category category) throws CategoryServiceException;

    void deleteCategoryById(Long id) throws CategoryServiceException;
}
