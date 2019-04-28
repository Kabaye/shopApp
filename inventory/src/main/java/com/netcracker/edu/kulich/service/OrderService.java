package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.entity.Customer;
import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.entity.Tag;
import com.netcracker.edu.kulich.service.exception.OrderServiceException;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order) throws OrderServiceException;

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order updateOrder(Order order) throws OrderServiceException;

    void deleteOrderById(Long id) throws OrderServiceException;

    List<OrderItem> findCustomerOrdersByCategory(Customer customer, String category) throws OrderServiceException;

    List<OrderItem> findCustomerOrdersByTag(Customer customer, Tag tag) throws OrderServiceException;
}
