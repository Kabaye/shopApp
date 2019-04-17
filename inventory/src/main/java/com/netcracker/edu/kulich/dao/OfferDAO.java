package com.netcracker.edu.kulich.dao;

import com.netcracker.edu.kulich.entity.OrderItem;

import java.util.List;

public interface OfferDAO {
    OrderItem create(OrderItem orderItem);

    OrderItem read(Long id);

    List<OrderItem> findAll();

    OrderItem update(OrderItem orderItem);

    void delete(Long id);
}
