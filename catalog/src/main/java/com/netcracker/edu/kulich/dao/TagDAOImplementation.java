package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Set;

public class TagDAOImplementation implements TagDAO {
    private EntityManager entityManager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

    @Override
    public Tag create(Tag tag) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(tag);
        transaction.commit();
        return tag;
    }

    @Override
    public Tag read(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Tag tag = entityManager.find(Tag.class, id);
        transaction.commit();
        return tag;
    }

    @Override
    public Set<Tag> create(Set<Tag> tags) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        for (Tag elem : tags) {
            entityManager.persist(elem);
        }
        transaction.commit();
        return tags;
    }

    @Override
    public Tag update(Tag tag) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        tag = entityManager.merge(tag);
        transaction.commit();
        return tag;
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Tag tag = entityManager.getReference(Tag.class, id);
        entityManager.refresh(tag);
        entityManager.remove(tag);
        transaction.commit();
    }
}
