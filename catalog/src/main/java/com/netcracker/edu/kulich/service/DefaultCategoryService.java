package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CategoryDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.exception.service.CategoryServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Transactional
@Service(value = "categoryService")
public class DefaultCategoryService implements CategoryService {
    private static final String NULL_CATEGORY_NAME_EXCEPTION_MESSAGE = "Category name is empty, please, set it not empty.";
    private static final String DELETING_OR_UPDATING_NOT_EXISTENT_CATEGORY = "You try to delete / update not existent category.";
    private static final String INSERTING_OR_UPDATING_CATEGORY_WITH_NOT_UNIQUE_NAME = "You try to insert / update category with already existent name, please, set name unique.";

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    public Category saveCategory(Category category) {
        checkCategoryHasNotNullNameAndUnique(category);
        categoryDAO.create(category);
        return category;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryDAO.read(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryDAO.readByName(name);
    }

    @Override
    public Set<Category> saveCategories(Set<Category> categories) {
        for (Category category : categories) {
            checkCategoryHasNotNullNameAndUnique(category);
        }
        categoryDAO.create(categories);
        return categories;
    }

    @Override
    public Category updateCategory(Category category) {
        checkCategoryHasNotNullNameAndUnique(category);
        Category category1 = categoryDAO.read(category.getId());
        if (category1 == null) {
            throw new CategoryServiceException(DELETING_OR_UPDATING_NOT_EXISTENT_CATEGORY);
        }
        category.setOffers(category1.getOffers());
        category = categoryDAO.update(category);
        return category;
    }

    @Override
    public void deleteCategoryById(Long id) throws CategoryServiceException {
        try {
            categoryDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new CategoryServiceException(DELETING_OR_UPDATING_NOT_EXISTENT_CATEGORY);
        }
    }

    private void checkCategoryHasNotNullNameAndUnique(Category category) throws CategoryServiceException {
        if (category.getCategory().equals("")) {
            throw new CategoryServiceException(NULL_CATEGORY_NAME_EXCEPTION_MESSAGE);
        }
        Category category1 = categoryDAO.readByName(category.getCategory());
        if (category1 != null) {
            throw new CategoryServiceException(INSERTING_OR_UPDATING_CATEGORY_WITH_NOT_UNIQUE_NAME);
        }
    }
}
