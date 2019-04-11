package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Set;

public class CategoryDAOImplementation implements CategoryDAO {
    private EntityManager entityManager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

    @Override
    public Category create(Category category) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(category);
        transaction.commit();
        return category;
    }

    @Override
    public Category read(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Category category = entityManager.find(Category.class, id);
        transaction.commit();
        return category;
    }

    @Override
    public Set<Category> create(Set<Category> categories) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (Category elem : categories) {
            entityManager.persist(elem);
        }
        transaction.commit();
        return categories;
    }

    /**
     * Merge the state of the given entity into the
     * current persistence context.
     * @param category entity instance
     * @return the managed instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an
     *         entity or is a removed (detached) entity
     */
    @Override
    public Category update(Category category) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        if (entityManager.contains(category)) {
            entityManager.merge(category);
        }
        else throw new IllegalArgumentException("Instance is not an entity or detached entity!");
        transaction.commit();
        return category;
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Category category = entityManager.getReference(Category.class, id);
        entityManager.refresh(category);
        entityManager.remove(category);
        transaction.commit();
    }
}
