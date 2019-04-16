package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Order;

import java.util.List;

public interface OrderDAO {
    Order create(Order order);

    Order read(Long id);

    List<Order> findAll();

    Order update(Order order);

    void delete(Long id);
}
