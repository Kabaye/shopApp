package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.Order;
import com.netcracker.edu.kulich.order.entity.OrderItem;
import com.netcracker.edu.kulich.order.entity.OrderPaymentStatusEnum;
import com.netcracker.edu.kulich.order.entity.Tag;

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
