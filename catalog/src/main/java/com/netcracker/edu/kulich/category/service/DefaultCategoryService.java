package com.netcracker.edu.kulich.category.service;

import com.netcracker.edu.kulich.category.dao.CategoryDAO;
import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.ServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "categoryService")
@DefaultLogging
public class DefaultCategoryService implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private CategoryDAO categoryDAO;
    private ServiceValidator<Category, Long> categoryValidator;
    private NameValidator categoryNameValidator;

    public DefaultCategoryService(CategoryDAO categoryDAO, ServiceValidator<Category, Long> categoryValidator, NameValidator categoryNameValidator) {
        this.categoryDAO = categoryDAO;
        this.categoryValidator = categoryValidator;
        this.categoryNameValidator = categoryNameValidator;
    }

    @Override
    @Logging(startMessage = "Request on saving category to database is received.", endMessage = "Category is successfully saved to database.")
    public Category saveCategory(Category category) {
        category.fixCategoryName();
        categoryValidator.checkForPersist(category);
        Category foundedCategory = categoryDAO.readByName(category.getCategory());
        if (foundedCategory != null) {
            return foundedCategory;
        }
        category = categoryDAO.create(category);
        return category;
    }

    @Override
    @Logging(startMessage = "Request on getting category by id from database is received.", endMessage = "Category is successfully get from database.")
    public Category getCategoryById(Long id) {
        categoryValidator.checkIdIsNotNull(id);
        return categoryDAO.read(id);
    }

    @Override
    @Logging(startMessage = "Request on getting category by name from database is received.", endMessage = "Category is successfully get from database.")
    public Category getCategoryByName(String name) {
        categoryNameValidator.check(name);
        return categoryDAO.readByName(name);
    }

    @Override
    @Logging(startMessage = "Request on saving some categories to database is received.", endMessage = "Categories are successfully saved to database.")
    public Set<Category> saveCategories(Set<Category> categories) {
        categories = categories.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        categories = categories.stream().map(this::saveCategory).collect(Collectors.toSet());
        return categories;
    }

    @Override
    @Logging(startMessage = "Request on updating category in database is received.", endMessage = "Category is successfully updated in database.")
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
    @Logging(startMessage = "Request on deleting category from database is received.", endMessage = "Category is successfully deleted from database.")
    public void deleteCategoryById(Long id) throws ServiceException {
        categoryValidator.checkIdIsNotNull(id);
        try {
            categoryDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            logger.error("Attempt to delete not existent category.");
            throw new ServiceException("Category with id: \"" + id + "\" doesn't exist. You can't delete not existent category.");
        }
    }
}
