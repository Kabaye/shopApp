package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.OrderItem;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface OrderItemDAO {
    OrderItem create(OrderItem orderItem);

    OrderItem read(Long id);

    List<OrderItem> findAll();

    OrderItem update(OrderItem orderItem);

    void delete(Long id) throws EntityNotFoundException;
}
