package com.netcracker.edu.kulich.category.dao;

import com.netcracker.edu.kulich.category.entity.Category;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

public interface CategoryDAO {
    Category create(Category category);

    Category read(Long id);

    Category readByName(String name);

    Set<Category> create(Set<Category> categories);

    Category update(Category category);

    void delete(Long id) throws EntityNotFoundException;
}
