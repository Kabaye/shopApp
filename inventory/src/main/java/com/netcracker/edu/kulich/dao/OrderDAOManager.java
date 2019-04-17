package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.*;
import com.netcracker.edu.kulich.utils.PostgreSQLDatabaseManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

public class OrderDAOManager implements OrderDAO {

    private EntityManagerFactory entityManagerFactory = PostgreSQLDatabaseManager.getDatabaseManagerFactory();
    private EntityManager entityManager;

    @Override
    public Order create(Order order) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        order = entityManager.merge(order);
        transaction.commit();
        entityManager.close();
        return order;
    }

    @Override
    public Order read(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Order foundOrder = entityManager.find(Order.class, id);
        if (foundOrder != null)
            entityManager.refresh(foundOrder);
        transaction.commit();
        entityManager.close();
        return foundOrder;
    }

    @Override
    public List<Order> findAll() {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        List<Order> orders;
        transaction.begin();
        orders = entityManager.createQuery("select order from Order order order by order.id", Order.class).getResultList();
        transaction.commit();
        entityManager.close();
        return orders;
    }


    @Override
    public Order update(Order order) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        order = entityManager.merge(order);
        transaction.commit();
        entityManager.close();
        return order;
    }

    @Override
    public void delete(Long id) {
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();
        Order order = entityManager.getReference(Order.class, id);
        entityManager.remove(order);
        transaction.commit();
        entityManager.close();
    }

    @Override
    public List<OrderItem> findCustomerOrdersByCategory(Customer customer, Category category) {
        List<OrderItem> orderItems;
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();

        Query query = entityManager.createQuery("SELECT o FROM OrderItem o" +
                " where o.category=:category AND o.order.customer = :customer");

        query.setParameter("customer", customer);
        query.setParameter("category", category);

        orderItems = query.getResultList();

        transaction.commit();
        entityManager.close();
        return orderItems;
    }

    @Override
    public List<OrderItem> findCustomerOrdersByTag(Customer customer, Tag tag) {
        List<OrderItem> orderItems;
        entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction;
        transaction = entityManager.getTransaction();
        transaction.begin();

        Query query = entityManager.createQuery("SELECT o FROM OrderItem o" +
                " where o.order.customer = :customer AND :tag MEMBER OF o.tags");

        query.setParameter("customer", customer);
        query.setParameter("tag", tag);

        orderItems = query.getResultList();

        transaction.commit();
        entityManager.close();
        return orderItems;
    }
}
