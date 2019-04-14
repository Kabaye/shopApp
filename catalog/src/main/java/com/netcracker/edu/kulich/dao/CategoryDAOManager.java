package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Set;

public class CategoryDAOManager implements CategoryDAO {
    private EntityManagerFactory entityManagerFactory = PostgreSQLDatabaseManager.getDatabaseManagerFactory();
    private EntityManager entityManager;

    @Override
    public Category create(Category category) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(category);
        transaction.commit();
        entityManager.close();
        return category;
    }

    @Override
    public Category read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Category category = entityManager.find(Category.class, id);
        transaction.commit();
        entityManager.close();
        return category;
    }

    @Override
    public Set<Category> create(Set<Category> categories) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (Category elem : categories) {
            entityManager.persist(elem);
        }
        transaction.commit();
        entityManager.close();
        return categories;
    }

    @Override
    public Category update(Category category) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(category);
        transaction.commit();
        entityManager.close();
        return category;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Category category = entityManager.getReference(Category.class, id);
        entityManager.refresh(category);
        entityManager.remove(category);
        transaction.commit();
        entityManager.close();
    }
}
