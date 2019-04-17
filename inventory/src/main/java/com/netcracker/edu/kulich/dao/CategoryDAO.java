package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;

import java.util.Set;

public interface CategoryDAO {
    Category create(Category category);

    Category read(Long id);

    Set<Category> create(Set<Category> categories);

    Category update(Category category);

    void delete(Long id);
}
