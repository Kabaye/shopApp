package com.netcracker.edu.kulich.order.service;

import com.netcracker.edu.kulich.order.entity.Order;
import com.netcracker.edu.kulich.order.entity.OrderItem;
import com.netcracker.edu.kulich.order.entity.Tag;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    Order payForOrder(Long id);

    Order nextStatus(Long id);

    Order cancelOrder(Long id);

    Order addOrderItem(Long id, OrderItem item);

    Order deleteOrderItem(Long id, Long itemId);

    void deleteOrderById(Long id);

    List<OrderItem> findCustomerOrderItemsByCategory(String customerEmail, String category);

    List<OrderItem> findCustomerOrderItemsByTag(String customerEmail, Tag tag);

    List<Order> getAllOrdersByPaymentStatus(String paymentStatus);

    List<Order> getAllOrdersByEmail(String email);

    Integer getAmountOfItemsBoughtByCustomerWithEmail(String email);

    Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email);
}
