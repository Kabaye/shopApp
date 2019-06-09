package com.netcracker.edu.kulich.order.dao;

import com.netcracker.edu.kulich.order.entity.OrderItem;

public interface OrderItemDAO {
    OrderItem read(Long id);
}
