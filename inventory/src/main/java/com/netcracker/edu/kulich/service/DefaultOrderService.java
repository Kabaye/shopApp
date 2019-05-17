package com.netcracker.edu.kulich.service;

import com.netcracker.edu.kulich.dao.OrderDAO;
import com.netcracker.edu.kulich.dao.OrderItemDAO;
import com.netcracker.edu.kulich.dao.TagDAO;
import com.netcracker.edu.kulich.entity.*;
import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.service.validation.EmailValidator;
import com.netcracker.edu.kulich.service.validation.NameValidator;
import com.netcracker.edu.kulich.service.validation.ServiceValidator;
import com.netcracker.edu.kulich.service.validation.TagValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service(value = "orderService")
public class DefaultOrderService implements OrderService {


    private OrderDAO orderDAO;
    private TagDAO tagDAO;
    private OrderItemDAO orderItemDAO;
    private NameValidator categoryNameValidator;
    private NameValidator tagNameValidator;
    private EmailValidator orderEmailValidator;
    private ServiceValidator<Order, Long> orderValidator;
    private TagValidator tagValidator;

    public DefaultOrderService(OrderDAO orderDAO, TagDAO tagDAO, OrderItemDAO orderItemDAO, NameValidator categoryNameValidator,
                               NameValidator tagNameValidator, EmailValidator orderEmailValidator, ServiceValidator<Order, Long> orderValidator, TagValidator tagValidator) {
        this.orderDAO = orderDAO;
        this.tagDAO = tagDAO;
        this.orderItemDAO = orderItemDAO;
        this.categoryNameValidator = categoryNameValidator;
        this.tagNameValidator = tagNameValidator;
        this.orderEmailValidator = orderEmailValidator;
        this.orderValidator = orderValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    public Order saveOrder(Order order) {
        order.fixAllNames();
        orderValidator.checkProperties(order);
        recreateOrder(order);
        order = orderDAO.create(order);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        orderValidator.checkIdIsNotNull(id);
        return orderDAO.read(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public Order payForOrder(Long id) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);
        order = orderDAO.update(order);
        return order;
    }

    @Override
    public Order nextStatus(Long id) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.NOT_PAID && order.getOrderStatus() == OrderStatusEnum.AGGREGATED) {
            throw new ServiceException("Sorry, but we can't send order to you without payment. Please, pay firstly and then we will send you your order.");
        }
        order.setOrderStatus(order.getOrderStatus().nextStatus());
        order = orderDAO.update(order);
        return order;
    }

    @Override
    public Order addOrderItem(Long id, OrderItem item) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.PAID || order.getOrderStatus() != OrderStatusEnum.IN_PROCESS) {
            throw new ServiceException("You can't update paid or shipped order.");
        }
        checkAndRecreateOrderItem(item);
        order.addItem(item);
        item.setOrder(order);
        order = orderDAO.update(order);
        order.postPersistAndUpdate();
        return order;
    }

    @Override
    public Order deleteOrderItem(Long id, Long itemId) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.PAID || order.getOrderStatus() != OrderStatusEnum.IN_PROCESS) {
            throw new ServiceException("You can't update paid or shipped order.");
        }
        OrderItem item = orderItemDAO.read(itemId);
        if (item != null) {
            if (order.getOrderItems().size() == 1 && order.getOrderItems().contains(item)) {
                throw new ServiceException("You trying to delete last item from order. Please, delete order instead of item.");
            }
            order.getOrderItems().remove(item);
            order = orderDAO.update(order);
            order.postPersistAndUpdate();
        }
        return order;
    }

    @Override
    public void deleteOrderById(Long id) {
        orderValidator.checkIdIsNotNull(id);
        try {
            orderDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            throw new ServiceException("Order with id: \'" + id + "\' doesn't exist. You can't delete not existent order.");
        }
    }

    @Override
    public List<OrderItem> findCustomerOrderItemsByCategory(String customerEmail, String category) {
        categoryNameValidator.check(category);
        orderEmailValidator.check(customerEmail);
        return orderDAO.findCustomerOrderItemsByCategory(customerEmail, category);
    }

    @Override
    public List<OrderItem> findCustomerOrderItemsByTag(String customerEmail, Tag tag) {
        tag.fixName();
        tagValidator.checkProperties(tag);
        Tag foundedTag = tagDAO.readByName(tag.getTagname());
        tagValidator.checkFoundByName(foundedTag, tag.getTagname());
        orderEmailValidator.check(customerEmail);
        return orderDAO.findCustomerOrderItemsByTag(customerEmail, foundedTag);
    }

    @Override
    public List<Order> getAllOrdersByPaymentStatus(String paymentStatus) {
        OrderPaymentStatusEnum orderPaymentStatus = OrderPaymentStatusEnum.valueOf(paymentStatus.toUpperCase());
        return orderDAO.getAllOrdersByPaymentStatus(orderPaymentStatus);
    }

    @Override
    public List<Order> getAllOrdersByEmail(String email) {
        orderEmailValidator.check(email);
        return orderDAO.getAllOrdersByEmail(email);
    }

    @Override
    public Integer getAmountOfItemsBoughtByCustomerWithEmail(String email) {
        orderEmailValidator.check(email);
        return orderDAO.getAllOrdersByEmail(email).stream()
                .mapToInt(Order::getAmountOfOrderItems)
                .sum();
    }

    @Override
    public Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email) {
        orderEmailValidator.check(email);
        return orderDAO.getAllOrdersByEmail(email).stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    private void recreateOrder(Order order) {
        if (order.getOrderItems().size() == 0) {
            throw new ServiceException("Order can't be empty. Please, add some offers.");
        }
        Set<OrderItem> items = new HashSet<>();
        for (OrderItem item : order.getOrderItems()) {
            items.add(checkAndRecreateOrderItem(item));
            item.setOrder(order);
        }
        order.setOrderItems(items);
    }

    private OrderItem checkAndRecreateOrderItem(OrderItem item) {
        Set<Tag> tags = recreateTags(item.getTags());
        item.setTags(tags);
        return item;
    }

    private Set<Tag> recreateTags(Set<Tag> tags) {
        Set<Tag> tagSet = new HashSet<>();
        for (Tag tag : tags) {
            tagSet.add(recreateTag(tag));
        }
        return tagSet;
    }

    private Tag recreateTag(Tag tag) {
        Tag t = tagDAO.readByName(tag.getTagname());
        if (t == null) {
            t = tagDAO.create(tag);
        }
        return t;
    }
}
