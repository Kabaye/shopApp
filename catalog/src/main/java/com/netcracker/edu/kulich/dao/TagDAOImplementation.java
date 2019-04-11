package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
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
        if (tag != null)
        entityManager.refresh(tag);
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

    /**
     * Merge the state of the given entity into the
     * current persistence context.
     * @param tag entity instance
     * @return the managed instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an
     *         entity or is a removed (detached) entity
     */
    @Override
    public Tag update(Tag tag) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        if (entityManager.contains(tag))
        tag = entityManager.merge(tag);
        else
        {
            transaction.commit();
            throw new IllegalArgumentException("Instance is not an entity or detached entity!");
        }
        transaction.commit();
        return tag;
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Tag tag = entityManager.getReference(Tag.class, id);
        entityManager.refresh(tag);

        Set<Offer> offers = tag.getOffers();

        for (Offer elem : offers ) {
            elem.getTags().remove(tag);
        }

        entityManager.remove(tag);
        transaction.commit();
    }
}
