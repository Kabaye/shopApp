package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import lombok.Getter;

@Getter
@Validator
public class CategoryValidator implements ServiceValidator<Category, Long> {

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
            throw new ServiceException(ID_NULL);
        }
    }

    @Override
    public void checkIdIsNotNull(Long id) {
        if (id == 0L) {
            throw new ServiceException(ID_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Category resource) {
        if (resource == null) {
            throw new ServiceException(CATEGORY_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Category resource, String errorMessage) {
        if (resource == null) {
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