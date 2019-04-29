package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.dao.OrderDAO;
import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.*;
import com.netcracker.edu.kulich.service.exception.OrderServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "orderService")
public class DefaultOrderService implements OrderService {

    private static final String NULL_ORDER_CUSTOMER_EXCEPTION_MESSAGE = "Customer wasn't set, please, set his/her.";
    private static final String NULL_ORDER_CUSTOMER_NAME_EXCEPTION_MESSAGE = "Customer name is empty, please, set it not empty.";
    private static final String NULL_ORDER_DATE = "Order date wasn't set, please, set it.";
    private static final String NULL_ORDER_PAYMENT_STATUS = "Order payment status wasn't set, please, set it.";
    private static final String NULL_ORDER_SHIPPING_STATUS = "Order shipping status wasn't set, please, set it.";
    private static final String NULL_ORDER_TAG_NAME = "Order tag name is empty, please, set it.";
    private static final String NULL_ORDER_ITEM_CATEGORY_NAME = "One of order's items has empty category, please, set it.";
    private static final String NULL_ORDER_ITEM_NAME = "One of order's items has empty name, please, set it";
    private static final String NULL_CATEGORY_NAME = "Category name is empty, please, set it";
    private static final String NULL_TAG_NAME = "Tag name is empty, please, set it";
    private static final String NULL_AMOUNT_OF_ORDER_ITEMS = "Amount of order items is 0, please, add, at least, 1 order item to your order.";

    private static final String NOT_CORRECT_CUSTOMER_AGE_IN_ORDER = "You set not correct customer age, please, set it correctly.";
    private static final String NOT_CORRECT_DATE_IN_ORDER = "You set not correct date, please, set it correctly.";
    private static final String NOT_CORRECT_ORDER_ITEM_PRICE = "One of order's items has not correct price, please, set it correctly.";

    private static final String DELETING_NOT_EXISTENT_ORDER = "You try to delete not existent order.";

    private static final LocalDate BEGINNING = LocalDate.of(2000, 1, 1);
    private static final LocalDate ENDING = LocalDate.now();

    private static final int MIN_AGE = 14;
    private static final int MAX_AGE = 122;
    private static final double MIN_PRICE = 1.0;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private TagDAO tagDAO;

    @Autowired
    private CustomerDAO customerDAO;

    @Override
    public Order saveOrder(Order order) throws OrderServiceException {
        orderCheckingAndRecreating(order);
        order = orderDAO.create(order);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderDAO.read(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public Order updateOrder(Order order) throws OrderServiceException {
        orderCheckingAndRecreating(order);
        order = orderDAO.update(order);
        return order;
    }

    @Override
    public void deleteOrderById(Long id) throws OrderServiceException {
        try {
            orderDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new OrderServiceException(DELETING_NOT_EXISTENT_ORDER);
        }
    }

    @Override
    public List<OrderItem> findCustomerOrdersByCategory(Customer customer, String category) throws OrderServiceException {
        customerChecking(customer);

        if (category.equals("")) {
            throw new OrderServiceException(NULL_CATEGORY_NAME);
        }

        return orderDAO.findCustomerOrdersByCategory(customer, category);
    }

    @Override
    public List<OrderItem> findCustomerOrdersByTag(Customer customer, Tag tag) throws OrderServiceException {
        customerChecking(customer);

        if (tag.getTagname().equals(""))
            throw new OrderServiceException(NULL_TAG_NAME);

        return orderDAO.findCustomerOrdersByTag(customer, tag);
    }

    private void orderCheckingAndRecreating(Order order) throws OrderServiceException {

        customerChecking(order.getCustomer());

        dateChecking(order.getDate());

        statusChecking(order.getOrderStatus(), order.getOrderPaymentStatus());

        if (order.getOrderItems().size() == 0) {
            throw new OrderServiceException(NULL_AMOUNT_OF_ORDER_ITEMS);
        }

        Set<OrderItem> items = new HashSet<>();

        for (OrderItem item : order.getOrderItems()) {
            items.add(orderItemCheckingAndRecreating(item));
        }

    }

    private void customerChecking(Customer customer) throws OrderServiceException {
        if (customer == null) {
            throw new OrderServiceException(NULL_ORDER_CUSTOMER_EXCEPTION_MESSAGE);
        }
        if (customer.getFio().equals("")) {
            throw new OrderServiceException(NULL_ORDER_CUSTOMER_NAME_EXCEPTION_MESSAGE);
        }
        if (customer.getAge() < MIN_AGE || customer.getAge() > MAX_AGE) {
            throw new OrderServiceException(NOT_CORRECT_CUSTOMER_AGE_IN_ORDER);
        }
    }

    private void dateChecking(LocalDate date) throws OrderServiceException {
        if (date == null) {
            throw new OrderServiceException(NULL_ORDER_DATE);
        }
        if (date.isBefore(BEGINNING) || date.isAfter(ENDING)) {
            throw new OrderServiceException(NOT_CORRECT_DATE_IN_ORDER);
        }
    }

    private void statusChecking(OrderStatusEnum orderStatus, OrderPaymentStatusEnum orderPaymentStatus) throws OrderServiceException {
        if (orderStatus == null) {
            throw new OrderServiceException(NULL_ORDER_SHIPPING_STATUS);
        }
        if (orderPaymentStatus == null) {
            throw new OrderServiceException(NULL_ORDER_PAYMENT_STATUS);
        }
    }

    private OrderItem orderItemCheckingAndRecreating(OrderItem item) throws OrderServiceException {
        Set<Tag> tags = tagsCheckingAndRecreating(item.getTags());
        item.setTags(tags);

        if (item.getPrice() < MIN_PRICE) {
            throw new OrderServiceException(NOT_CORRECT_ORDER_ITEM_PRICE);
        }

        if (item.getCategory().equals("")) {
            throw new OrderServiceException(NULL_ORDER_ITEM_CATEGORY_NAME);
        }

        if (item.getName().equals("")) {
            throw new OrderServiceException(NULL_ORDER_ITEM_NAME);
        }

        return item;
    }

    private Set<Tag> tagsCheckingAndRecreating(Set<Tag> tags) throws OrderServiceException {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Tag> tagSet = new HashSet<>();
        for (Tag tag : tags) {
            if (tag.getTagname().equals("")) {
                throw new OrderServiceException(NULL_ORDER_TAG_NAME);
            }
            Tag t = tagDAO.readByName(tag.getTagname());
            tagSet.add(t == null ? tag : t);
        }
        return tagSet;
    }
}
