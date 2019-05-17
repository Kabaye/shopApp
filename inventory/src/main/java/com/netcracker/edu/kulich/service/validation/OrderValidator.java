package com.netcracker.edu.kulich.service.validation;

import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.exception.service.ServiceException;

@Validator
public class OrderValidator implements ServiceValidator<Order, Long> {
    private static final String ID_NOT_NULL = "Order id must be initialized.";
    private static final String ID_NULL = "Order id must not be initialized.";
    private static final String ORDER_DOESNT_EXIST = "Order with such id doesn't exist. Please, set id valid.";
    private static final String ORDER_NOT_NULL = "Order can't be null.";

    private final TagValidator tagValidator;
    private final NameValidator categoryNameValidator;
    private final EmailValidator orderEmailValidator;
    private final DateValidator orderDateValidator;

    public OrderValidator(TagValidator tagValidator, CategoryNameValidator categoryNameValidator, EmailValidator orderEmailValidator, DateValidator orderDateValidator) {
        this.tagValidator = tagValidator;
        this.categoryNameValidator = categoryNameValidator;
        this.orderEmailValidator = orderEmailValidator;
        this.orderDateValidator = orderDateValidator;
    }

    @Override
    public Long extractId(Order resource) {
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
    public void checkNotNull(Order resource) {
        if (resource == null) {
            throw new ServiceException(ORDER_NOT_NULL);
        }
    }

    @Override
    public void checkNotNull(Order resource, String errorMessage) {
        if (resource == null) {
            throw new ServiceException(errorMessage);
        }
    }

    @Override
    public void checkFoundById(Order resource) {
        checkNotNull(resource, ORDER_DOESNT_EXIST);
    }

    @Override
    public void checkProperties(Order resource) {
        orderDateValidator.check(resource.getDate());
        orderEmailValidator.check(resource.getEmail());
        resource.getOrderItems().forEach(orderItem -> orderItem.getTags().forEach(tagValidator::checkProperties));
        resource.getOrderItems().forEach(item -> categoryNameValidator.check(item.getCategory()));
    }
}