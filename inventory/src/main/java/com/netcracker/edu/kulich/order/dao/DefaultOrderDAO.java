package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.Order;
import com.netcracker.edu.kulich.order.entity.OrderItem;
import com.netcracker.edu.kulich.order.entity.OrderPaymentStatusEnum;
import com.netcracker.edu.kulich.order.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
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
        return entityManager.createNamedQuery("Order.getAll", Order.class).getResultList();
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
        return entityManager.createNamedQuery("OrderItem.getAllByCustomerAndCategory", OrderItem.class)
                .setParameter("category", category)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public List<OrderItem> findCustomerOrderItemsByTag(String email, Tag tag) {
        return entityManager.createNamedQuery("OrderItem.getAllByCustomerAndTag", OrderItem.class)
                .setParameter("email", email)
                .setParameter("tag", tag)
                .getResultList();
    }

    @Override
    public List<Order> getAllOrdersByPaymentStatus(OrderPaymentStatusEnum paymentStatus) {
        return entityManager.createNamedQuery("Order.getAllByStatus", Order.class)
                .setParameter("status", paymentStatus)
                .getResultList();
    }

    @Override
    public List<Order> getAllOrdersByEmail(String email) {
        return entityManager.createNamedQuery("Order.getAllByEmail", Order.class)
                .setParameter("email", email)
                .getResultList();
    }
}
