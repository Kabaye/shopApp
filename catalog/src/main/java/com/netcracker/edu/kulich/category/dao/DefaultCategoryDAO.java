package com.netcracker.edu.kulich.category.dao;

import com.netcracker.edu.kulich.category.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Set;

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
        return entityManager.find(Category.class, id);
    }

    @Override
    public Category readByName(String name) {
        return entityManager.createNamedQuery("Category.findByName", Category.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList()
                .stream()
                .limit(1)
                .findFirst()
                .orElse(null);
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
    public void delete(Long id) throws EntityNotFoundException {
        Category category = entityManager.find(Category.class, id);
        if (category == null)
            throw new EntityNotFoundException();
        entityManager.refresh(category);
        entityManager.remove(category);
    }
}
