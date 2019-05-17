package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.entity.OrderPaymentStatusEnum;
import com.netcracker.edu.kulich.entity.Tag;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface OrderDAO {
    Order create(Order order);

    Order read(Long id);

    List<Order> findAll();

    Order update(Order order);

    void delete(Long id) throws EntityNotFoundException;

    List<OrderItem> findCustomerOrderItemsByCategory(String email, String category);

    List<OrderItem> findCustomerOrderItemsByTag(String email, Tag tag);

    List<Order> getAllOrdersByPaymentStatus(OrderPaymentStatusEnum paymentStatus);

    List<Order> getAllOrdersByEmail(String email);
}
