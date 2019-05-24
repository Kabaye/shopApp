package com.netcracker.edu.kulich.customer.service.validation;

public interface ServiceValidator<T, I> {
    I extractId(T resource);

    void checkIdIsNotNull(I id);

    void checkNotNull(T resource);

    void checkNotNull(T resource, String errorMessage);

    void checkFoundById(T resource);

    void checkProperties(T resource);

    default void checkForPersist(T resource) {
        checkNotNull(resource);
        checkIdIsNotNull(extractId(resource));
        checkProperties(resource);
    }

    default void checkForUpdate(T resource) {
        checkNotNull(resource);
        checkIdIsNotNull(extractId(resource));
    }
}
