package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CategoryDAO;
import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.service.validation.CategoryValidator;
import com.netcracker.edu.kulich.service.validation.NameValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "categoryService")
public class DefaultCategoryService implements CategoryService {

    private CategoryDAO categoryDAO;
    private CategoryValidator categoryValidator;
    private NameValidator categoryNameValidator;

    public DefaultCategoryService(CategoryDAO categoryDAO, CategoryValidator categoryValidator, NameValidator categoryNameValidator) {
        this.categoryDAO = categoryDAO;
        this.categoryValidator = categoryValidator;
        this.categoryNameValidator = categoryNameValidator;
    }

    @Override
    public Category saveCategory(Category category) {
        category.fixCategoryName();
        categoryValidator.checkForPersist(category);
        Category findedCategory = categoryDAO.readByName(category.getCategory());
        if (findedCategory != null) {
            return findedCategory;
        }
        category = categoryDAO.create(category);
        return category;
    }

    @Override
    public Category getCategoryById(Long id) {
        categoryValidator.checkIdIsNotNull(id);
        return categoryDAO.read(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        categoryNameValidator.check(name);
        return categoryDAO.readByName(name);
    }

    @Override
    public Set<Category> saveCategories(Set<Category> categories) {
        categories = categories.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        categories = categories.stream().map(this::saveCategory).collect(Collectors.toSet());
        return categories;
    }

    @Override
    public Category updateCategoryByName(Category category) {
        category.fixCategoryName();
        categoryValidator.checkForUpdate(category);
        Category foundedCategory = categoryDAO.read(category.getId());
        categoryValidator.checkFoundById(foundedCategory);
        category.setOffers(foundedCategory.getOffers());
        category = categoryDAO.update(category);
        return category;
    }

    @Override
    public void deleteCategoryById(Long id) throws ServiceException {
        categoryValidator.checkIdIsNotNull(id);
        try {
            categoryDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new ServiceException("Category with id: \"" + id + "\" doesn't exist. You can't delete not existent category.");
        }
    }
}
