package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.entity.OrderPaymentStatusEnum;
import com.netcracker.edu.kulich.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

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
    public void delete(Long id) throws EntityNotFoundException {
        Order order = entityManager.find(Order.class, id);
        if (order == null) {
            throw new EntityNotFoundException();
        }
        entityManager.remove(order);
    }

    @Override
    public List<OrderItem> findCustomerOrderItemsByCategory(String email, String category) {
        List<OrderItem> orderItems;

        Query query = entityManager.createQuery("SELECT o FROM OrderItem o" +
                " where o.category=:category AND o.order.email = :email");

        query.setParameter("email", email);
        query.setParameter("category", category);

        orderItems = query.getResultList();

        return orderItems;
    }

    @Override
    public List<OrderItem> findCustomerOrderItemsByTag(String email, Tag tag) {
        List<OrderItem> orderItems;

        Query query = entityManager.createQuery("SELECT o FROM OrderItem o" +
                " where o.order.email = :email AND :tag MEMBER OF o.tags");

        query.setParameter("email", email);
        query.setParameter("tag", tag);

        orderItems = query.getResultList();

        return orderItems;
    }

    @Override
    public List<Order> getAllOrdersByPaymentStatus(OrderPaymentStatusEnum paymentStatus) {
        return entityManager.createQuery("SELECT order FROM Order order WHERE order.orderPaymentStatus = :status")
                .setParameter("status", paymentStatus).getResultList();
    }

    @Override
    public List<Order> getAllOrdersByEmail(String email) {
        return entityManager.createQuery("SELECT order FROM Order order WHERE order.email = :email")
                .setParameter("email", email).getResultList();
    }
}
