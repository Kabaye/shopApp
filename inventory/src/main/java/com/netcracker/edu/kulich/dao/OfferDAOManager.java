package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

public class OfferDAOManager implements OfferDAO {

    private EntityManagerFactory entityManagerFactory = PostgreSQLDatabaseManager.getDatabaseManagerFactory();
    private EntityManager entityManager;

    @Override
    public OrderItem create(OrderItem orderItem) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        orderItem = entityManager.merge(orderItem);
        transaction.commit();
        entityManager.close();
        return orderItem;
    }

    @Override
    public OrderItem read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        OrderItem foundOrderItem = entityManager.find(OrderItem.class, id);
        entityManager.refresh(foundOrderItem);
        transaction.commit();
        entityManager.close();
        return foundOrderItem;
    }

    @Override
    public List<OrderItem> findAll() {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<OrderItem> orderItems;
        transaction.begin();
        orderItems = entityManager.createQuery("select offer from OrderItem offer order by offer.id", OrderItem.class).getResultList();
        transaction.commit();
        entityManager.close();
        return orderItems;
    }


    @Override
    public OrderItem update(OrderItem orderItem) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        orderItem = entityManager.merge(orderItem);
        transaction.commit();
        entityManager.close();
        return orderItem;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        OrderItem orderItem = entityManager.getReference(OrderItem.class, id);
        entityManager.remove(orderItem);
        transaction.commit();
        entityManager.close();
    }
}
