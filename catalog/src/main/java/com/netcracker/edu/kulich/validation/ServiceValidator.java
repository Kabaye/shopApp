package com.netcracker.edu.kulich.validation;

public interface ServiceValidator<T, I> {
    I extractId(T resource);

    void checkIdIsNull(I id);

    void checkIdIsNotNull(I id);

    void checkNotNull(T resource);

    void checkNotNull(T resource, String errorMessage);

    void checkFoundById(T resource);

    void checkProperties(T resource);

    default void checkFoundByName(T resource, String name) {
    }

    default void checkForPersist(T resource) {
        checkNotNull(resource);
        checkIdIsNull(extractId(resource));
        checkProperties(resource);
    }

    default void checkForUpdate(T resource) {
        checkNotNull(resource);
        checkIdIsNotNull(extractId(resource));
    }
}
