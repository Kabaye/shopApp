package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository(value = "tagDAO")
public class DefaultTagDAO implements TagDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Tag readByName(String name) {
        return entityManager.createNamedQuery("Tag.findByName", Tag.class)
                .setParameter("tagname", name)
                .setMaxResults(1).getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
