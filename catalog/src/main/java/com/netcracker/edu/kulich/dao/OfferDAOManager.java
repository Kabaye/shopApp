package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OfferDAOManager implements OfferDAO {

    private EntityManager entityManager = PostgreSQLDatabaseManager.getInstance().getEntityManager();

    @Override
    public Offer create(Offer offer) {
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(offer);
        transaction.commit();
        entityManager.detach(offer);
        return offer;
    }

    @Override
    public Offer read(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Offer foundOffer = entityManager.find(Offer.class, id);

        //check problem with lazy downloading

        foundOffer.toString();
        transaction.commit();
        entityManager.detach(foundOffer);
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


    @Override
    public Offer update(Offer offer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(offer);
        transaction.commit();
        entityManager.detach(offer);
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
