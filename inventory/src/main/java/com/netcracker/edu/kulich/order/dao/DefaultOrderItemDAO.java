package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository(value = "orderItemDAO")
public class DefaultOrderItemDAO implements OrderItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderItem read(Long id) {
        OrderItem foundOrderItem = entityManager.find(OrderItem.class, id);
        if (foundOrderItem != null)
            entityManager.refresh(foundOrderItem);
        return foundOrderItem;
    }
}
