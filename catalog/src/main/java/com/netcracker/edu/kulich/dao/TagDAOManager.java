package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Set;

public class TagDAOManager implements TagDAO {
    private EntityManagerFactory entityManagerFactory = PostgreSQLDatabaseManager.getDatabaseManagerFactory();
    private EntityManager entityManager;

    @Override
    public Tag create(Tag tag) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(tag);
        entityManager.getTransaction().commit();
        entityManager.close();
        return tag;
    }

    @Override
    public Tag read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Tag tag = entityManager.find(Tag.class, id);
        //entityManager.refresh(tag);
        entityManager.getTransaction().commit();
        entityManager.close();
        return tag;
    }

    @Override
    public Set<Tag> create(Set<Tag> tags) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for (Tag elem : tags) {
            entityManager.persist(elem);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
        return tags;
    }


    @Override
    public Tag update(Tag tag) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        tag = entityManager.merge(tag);
        entityManager.getTransaction().commit();
        entityManager.close();
        return tag;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Tag tag = entityManager.getReference(Tag.class, id);
        for (Offer offer : tag.getOffers()
        ) {
            offer.getTags().remove(tag);
        }
        entityManager.remove(tag);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
