package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
@Repository(value = "orderDAO")
public class DefaultOrderDAO implements OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order create(Order order) {
        order = entityManager.merge(order);
        return order;
    }

    @Override
    public Order read(Long id) {
        Order foundOrder = entityManager.find(Order.class, id);
        if (foundOrder != null)
            entityManager.refresh(foundOrder);
        return foundOrder;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders;
        orders = entityManager.createQuery("select order from Order order order by order.id", Order.class).getResultList();
        return orders;
    }


    @Override
    public Order update(Order order) {
        order = entityManager.merge(order);
        return order;
    }

    @Override
    public void delete(Long id) {
        Order order = entityManager.getReference(Order.class, id);
        entityManager.remove(order);
    }

    @Override
    public List<OrderItem> findCustomerOrdersByCategory(Customer customer, Category category) {
        List<OrderItem> orderItems;

        Query query = entityManager.createQuery("SELECT o FROM OrderItem o" +
                " where o.category=:category AND o.order.customer = :customer");

        query.setParameter("customer", customer);
        query.setParameter("category", category);

        orderItems = query.getResultList();

        return orderItems;
    }

    @Override
    public List<OrderItem> findCustomerOrdersByTag(Customer customer, Tag tag) {
        List<OrderItem> orderItems;

        Query query = entityManager.createQuery("SELECT o FROM OrderItem o" +
                " where o.order.customer = :customer AND :tag MEMBER OF o.tags");

        query.setParameter("customer", customer);
        query.setParameter("tag", tag);

        orderItems = query.getResultList();

        return orderItems;
    }
}
