package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;

@Transactional
@Repository(value = "categoryDAO")
public class DefaultCategoryDAO implements CategoryDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Category create(Category category) {
        entityManager.persist(category);
        return category;
    }

    @Override
    public Category read(Long id) {
        Category category = entityManager.find(Category.class, id);
        return category;
    }

    @Override
    public Set<Category> create(Set<Category> categories) {
        for (Category elem : categories) {
            entityManager.persist(elem);
        }
        return categories;
    }

    @Override
    public Category update(Category category) {
        category = entityManager.merge(category);
        return category;
    }

    @Override
    public void delete(Long id) {
        Category category = entityManager.getReference(Category.class, id);
        entityManager.refresh(category);
        entityManager.remove(category);
    }
}
