package com.netcracker.edu.kulich.category.validation;

import com.netcracker.edu.kulich.category.entity.Category;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.ServiceValidator;
import com.netcracker.edu.kulich.validation.Validator;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Validator
public class CategoryValidator implements ServiceValidator<Category, Long> {
    private static final Logger logger = LoggerFactory.getLogger(ServiceValidator.class);
    private static final String ID_NOT_NULL = "Category id must be initialized.";
    private static final String ID_NULL = "Category id must not be initialized.";
    private static final String CATEGORY_DOESNT_EXIST = "Category with such id doesn't exist. Please, set id valid.";
    private static final String CATEGORY_NOT_NULL = "Category can't be null.";

    private NameValidator categoryNameValidator;

    public CategoryValidator(NameValidator categoryNameValidator) {
        this.categoryNameValidator = categoryNameValidator;
    }

    @Override
    public Long extractId(Category resource) {
        return resource.getId();
    }

    @Override
    public void checkIdIsNull(Long id) {
        if (id != 0L) {
            logger.error("Id of category is not NULL.");
            throw new ServiceException(ID_NULL);
        }
    }

    @Override
    public void checkIdIsNotNull(Long id) {
        if (id == 0L) {
            logger.error("Id of category is NULL.");
            throw new ServiceException(ID_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Category resource) {
        if (resource == null) {
            logger.error("Category is NULL.");
            throw new ServiceException(CATEGORY_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Category resource, String errorMessage) {
        if (resource == null) {
            logger.error("Category is NULL.");
            throw new ServiceException(errorMessage);
        }
    }

    @Override
    public void checkFoundById(Category resource) {
        checkNotNull(resource, CATEGORY_DOESNT_EXIST);
    }

    public void checkFoundByName(Category resource, String name) {
        checkNotNull(resource, "Category with name: \"" + name + "\" doesn't exist. Please, firstly, create category and then, create offer with such category.");
    }

    @Override
    public void checkProperties(Category resource) {
        categoryNameValidator.check(resource.getCategory());
    }
}