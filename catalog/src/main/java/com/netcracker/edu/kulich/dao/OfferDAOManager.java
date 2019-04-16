package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Offer;
import com.netcracker.edu.kulich.utils.PostgreSQLEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OfferDAOManager implements OfferDAO {

    private EntityManagerFactory entityManagerFactory = PostgreSQLEntityManagerFactory.getDatabaseManagerFactory();
    private EntityManager entityManager;

    @Override
    public Offer create(Offer offer) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        offer = entityManager.merge(offer);
        transaction.commit();
        entityManager.close();
        return offer;
    }

    @Override
    public Offer read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Offer foundOffer = entityManager.find(Offer.class, id);
        entityManager.refresh(foundOffer);
        transaction.commit();
        entityManager.close();
        return foundOffer;
    }

    @Override
    public List<Offer> findAll() {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Offer> offers;
        transaction.begin();
        offers = entityManager.createQuery("select offer from Offer offer order by offer.id", Offer.class).getResultList();
        transaction.commit();
        entityManager.close();
        return offers;
    }


    @Override
    public Offer update(Offer offer) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(offer);
        transaction.commit();
        entityManager.close();
        return offer;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        Offer offer = entityManager.getReference(Offer.class, id);
        entityManager.remove(offer);
        transaction.commit();
        entityManager.close();
    }
}
