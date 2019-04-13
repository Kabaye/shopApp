package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Set;

public class CategoryDAOManager implements CategoryDAO {
    private EntityManager entityManager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

    @Override
    public Category create(Category category) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(category);
        transaction.commit();
        entityManager.detach(category);
        return category;
    }

    @Override
    public Category read(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            entityManager.refresh(category);
            entityManager.detach(category);
        }
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
        for (Category elem : categories) {
            entityManager.detach(elem);
        }
        return categories;
    }

    @Override
    public Category update(Category category) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(category);
        entityManager.detach(category);
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
