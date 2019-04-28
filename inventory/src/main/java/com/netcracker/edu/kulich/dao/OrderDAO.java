package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.entity.Tag;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface OrderDAO {
    Order create(Order order);

    Order read(Long id);

    List<Order> findAll();

    Order update(Order order);

    void delete(Long id) throws EntityNotFoundException;

    List<OrderItem> findCustomerOrdersByCategory(Customer customer, String category);

    List<OrderItem> findCustomerOrdersByTag(Customer customer, Tag tag);
}
