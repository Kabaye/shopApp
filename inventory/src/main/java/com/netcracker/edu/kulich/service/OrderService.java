package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.entity.Tag;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateCustomer(Long id, Customer customer);

    Order updatePaymentStatus(Long id, String paymentStatus);

    Order updateStatus(Long id, String status);

    Order addOrderItem(Long id, OrderItem item);

    Order deleteOrderItem(Long id, Long itemId);

    void deleteOrderById(Long id);

    List<OrderItem> findCustomerOrdersByCategory(Long customerId, String category);

    List<OrderItem> findCustomerOrdersByTag(Long customerId, Tag tag);
}
