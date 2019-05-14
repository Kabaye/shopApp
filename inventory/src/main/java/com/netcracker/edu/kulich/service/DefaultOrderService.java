package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.CustomerDAO;
import com.netcracker.edu.kulich.dao.OrderDAO;
import com.netcracker.edu.kulich.dao.OrderItemDAO;
import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.*;
import com.netcracker.edu.kulich.exception.service.OrderServiceException;
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

    private static final String NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE = "One of order's arguments wasn't set, please, set his/her.";

    private static final String ORDER_ARGUMENT_NOT_VALID = "One of order arguments is not valid, please, set it valid";

    private static final String DELETING_NOT_EXISTENT_ORDER = "You try to delete not existent order.";
    private static final String DELETING_NOT_EXISTENT_ORDER_ITEM = "You try to delete not existent order item from order.";
    private static final String DELETING_LAST_ORDER_ITEM = "You try to delete last order item from order, in such case, delete order.";
    private static final String ID_OF_NOT_EXISTING_CUSTOMER = "Customer with such ID, that you set, doesn't exist.";
    private static final String TAG_NOT_EXIST = "Tag with such tagname doesn't exist, so, you can't find anything with this tag. Please, set existent one.";

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

    @Autowired
    private OrderItemDAO orderItemDAO;

    @Override
    public Order saveOrder(Order order) {
        order.fixAllNames();
        checkAndRecreateOrder(order);
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
    public Order updateCustomer(Long id, Customer customer) {
        Order order = orderDAO.read(id);
        if (order == null) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        customer.fixName();
        if (customer.getAge() == 0 && customer.getFio().length() == 0) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }
        if (customer.getAge() == 0) {
            customer.setAge(order.getCustomer().getAge());
        }
        if (customer.getFio().length() == 0) {
            customer.setFio(order.getCustomer().getFio());
        }
        checkCustomer(customer);
        customer.setId(order.getCustomer().getId());
        order.setCustomer(customer);
        order = orderDAO.update(order);
        return order;
    }

    @Override
    public Order updatePaymentStatus(Long id, String paymentStatus) {
        paymentStatus = paymentStatus.trim();
        Order order = orderDAO.read(id);
        if (order == null) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.valueOf(paymentStatus.toUpperCase()));
        order = orderDAO.update(order);
        return order;
    }

    @Override
    public Order updateStatus(Long id, String status) {
        status = status.trim();
        Order order = orderDAO.read(id);
        if (order == null) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        order.setOrderStatus(OrderStatusEnum.valueOf(status.toUpperCase()));
        order = orderDAO.update(order);
        return order;
    }

    @Override
    public Order addOrderItem(Long id, OrderItem item) {
        Order order = orderDAO.read(id);
        if (order == null) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        item.fixName();
        checkAndRecreateOrderItem(item);
        if (!order.addOffer(item)) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        item.setOrder(order);
        order = orderDAO.update(order);
        order.postPersistAndUpdate();
        return order;
    }

    @Override
    public Order deleteOrderItem(Long id, Long itemId) {
        Order order = orderDAO.read(id);
        if (order == null) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        OrderItem item = orderItemDAO.read(itemId);
        if (item == null) {
            throw new OrderServiceException(DELETING_NOT_EXISTENT_ORDER_ITEM);
        }
        if (order.getOrderItems().size() == 1 && order.getOrderItems().contains(item)) {
            throw new OrderServiceException(DELETING_LAST_ORDER_ITEM);
        }
        order.getOrderItems().remove(item);
        order = orderDAO.update(order);
        order.postPersistAndUpdate();
        return order;
    }

    @Override
    public void deleteOrderById(Long id) {
        try {
            orderDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new OrderServiceException(DELETING_NOT_EXISTENT_ORDER);
        }
    }

    @Override
    public List<OrderItem> findCustomerOrdersByCategory(Long customerId, String category) {
        Customer customer = customerDAO.getById(customerId);

        if (customer == null) {
            throw new OrderServiceException(ID_OF_NOT_EXISTING_CUSTOMER);
        }
        category = category.trim();
        if (category.equals("")) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }

        return orderDAO.findCustomerOrdersByCategory(customer, category);
    }

    @Override
    public List<OrderItem> findCustomerOrdersByTag(Long customerId, Tag tag) {
        tag.fixName();
        Customer customer = customerDAO.getById(customerId);

        if (customer == null) {
            throw new OrderServiceException(ID_OF_NOT_EXISTING_CUSTOMER);
        }

        if (tagDAO.readByName(tag.getTagname()) == null) {
            throw new OrderServiceException(TAG_NOT_EXIST);
        }

        tag = checkAndRecreateTag(tag);

        return orderDAO.findCustomerOrdersByTag(customer, tag);
    }

    private void checkAndRecreateOrder(Order order) {

        checkCustomer(order.getCustomer());

        checkDate(order.getDate());

        checkStatuses(order.getOrderStatus(), order.getOrderPaymentStatus());

        if (order.getOrderItems().size() == 0) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }

        Set<OrderItem> items = new HashSet<>();

        for (OrderItem item : order.getOrderItems()) {
            items.add(checkAndRecreateOrderItem(item));
            item.setOrder(order);
        }

        order.setOrderItems(items);
    }

    private void checkCustomer(Customer customer) {
        if (customer == null) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }
        if (customer.getFio().length() < 3) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        if (customer.getAge() < MIN_AGE || customer.getAge() > MAX_AGE) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
    }

    private void checkDate(LocalDate date) {
        if (date == null) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }
        if (date.isBefore(BEGINNING) || date.isAfter(ENDING)) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
    }

    private void checkStatuses(OrderStatusEnum orderStatus, OrderPaymentStatusEnum orderPaymentStatus) {
        if (orderStatus == null) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }
        if (orderPaymentStatus == null) {
            throw new OrderServiceException(NULL_ORDER_ARGUMENT_EXCEPTION_MESSAGE);
        }
    }

    private OrderItem checkAndRecreateOrderItem(OrderItem item) {
        Set<Tag> tags = checkAndRecreateTags(item.getTags());
        item.setTags(tags);

        if (item.getPrice() < MIN_PRICE) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }

        if (item.getCategory().length() < 2) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }

        if (item.getName().length() < 3) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }

        return item;
    }

    private Set<Tag> checkAndRecreateTags(Set<Tag> tags) {
        tags = tags.stream().filter(Objects::nonNull).collect(Collectors.toSet());
        Set<Tag> tagSet = new HashSet<>();
        for (Tag tag : tags) {
            tagSet.add(checkAndRecreateTag(tag));
        }
        return tagSet;
    }

    private Tag checkAndRecreateTag(Tag tag) {
        if (tag.getTagname().length() < 3) {
            throw new OrderServiceException(ORDER_ARGUMENT_NOT_VALID);
        }
        Tag t = tagDAO.readByName(tag.getTagname());
        if (t == null) {
            t = tagDAO.create(tag);
        }
        return t;
    }
}
