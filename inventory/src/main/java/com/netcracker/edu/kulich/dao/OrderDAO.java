package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.*;

import java.util.List;

public interface OrderDAO {
    Order create(Order order);

    Order read(Long id);

    List<Order> findAll();

    Order update(Order order);

    void delete(Long id);

    List<OrderItem> findCustomerOrdersByCategory(Customer customer, Category category);

    List<OrderItem> findCustomerOrdersByTag(Customer customer, Tag tag);
}
