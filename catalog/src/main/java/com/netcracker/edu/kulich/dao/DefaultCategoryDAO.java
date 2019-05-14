package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
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
        Category category = entityManager.find(Category.class, id);
        return category;
    }

    @Override
    public Category readByName(String name) {
        Category category;
        List objects = entityManager.createQuery("SELECT c FROM Category c WHERE c.category = :name ")
                .setParameter("name", name)
                .getResultList();
        if (objects.size() == 0)
            category = null;
        else
            category = (Category) objects.get(0);
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
    public void delete(Long id) throws EntityNotFoundException {
        Category category = entityManager.find(Category.class, id);
        if (category == null)
            throw new EntityNotFoundException();
        entityManager.refresh(category);
        entityManager.remove(category);
    }
}
