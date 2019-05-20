package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository(value = "orderitemDAO")
public class DefaultOrderItemDAO implements OrderItemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrderItem create(OrderItem orderItem) {
        orderItem = entityManager.merge(orderItem);
        return orderItem;
    }

    @Override
    public OrderItem read(Long id) {
        OrderItem foundOrderItem = entityManager.find(OrderItem.class, id);
        if (foundOrderItem != null)
            entityManager.refresh(foundOrderItem);
        return foundOrderItem;
    }

    @Override
    public List<OrderItem> findAll() {
        List<OrderItem> orderItems;
        orderItems = entityManager.createQuery("select offer from OrderItem offer order by offer.id", OrderItem.class).getResultList();
        return orderItems;
    }


    @Override
    public OrderItem update(OrderItem orderItem) {
        orderItem = entityManager.merge(orderItem);
        return orderItem;
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        OrderItem orderItem = entityManager.find(OrderItem.class, id);
        if (orderItem == null) {
            throw new EntityNotFoundException();
        }
        entityManager.remove(orderItem);
    }
}
