package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OfferDAOImplementation implements OfferDAO {

    private EntityManager entityManager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

    @Override
    public Offer create(Offer offer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(offer);
        transaction.commit();
        return offer;
    }

    @Override
    public Offer read(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Offer foundOffer = entityManager.find(Offer.class, id);
        transaction.commit();
        return foundOffer;
    }

    @Override
    public List<Offer> findAll() {
        EntityTransaction transaction = entityManager.getTransaction();
        List<Offer> offers;
        transaction.begin();
        offers = entityManager.createQuery("select offer from Offer offer", Offer.class).getResultList();
        transaction.commit();
        return offers;
    }

    /**
     * Merge the state of the given entity into the
     * current persistence context.
     * @param offer entity instance
     * @return the managed instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an
     *         entity or is a removed (detached) entity
     */
    @Override
    public Offer update(Offer offer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        if (entityManager.contains(offer)) {
            entityManager.merge(offer);
        }
        else throw new IllegalArgumentException("Instance is not an entity or detached entity!");
        transaction.commit();
        return offer;
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Offer offer = entityManager.getReference(Offer.class, id);
        entityManager.remove(offer);
        transaction.commit();
    }
}
