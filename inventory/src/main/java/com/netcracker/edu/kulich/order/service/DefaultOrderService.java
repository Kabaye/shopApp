package com.netcracker.edu.kulich.order.service;

import com.netcracker.edu.kulich.exception.service.ServiceException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import com.netcracker.edu.kulich.order.dao.OrderDAO;
import com.netcracker.edu.kulich.order.dao.OrderItemDAO;
import com.netcracker.edu.kulich.order.dao.TagDAO;
import com.netcracker.edu.kulich.order.entity.*;
import com.netcracker.edu.kulich.validation.EmailValidator;
import com.netcracker.edu.kulich.validation.NameValidator;
import com.netcracker.edu.kulich.validation.ServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Transactional
@Service(value = "orderService")
@DefaultLogging
public class DefaultOrderService implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private OrderDAO orderDAO;
    private TagDAO tagDAO;
    private OrderItemDAO orderItemDAO;
    private NameValidator categoryNameValidator;
    private EmailValidator orderEmailValidator;
    private ServiceValidator<Order, Long> orderValidator;
    private ServiceValidator<Tag, Long> tagValidator;

    public DefaultOrderService(OrderDAO orderDAO, TagDAO tagDAO, OrderItemDAO orderItemDAO,
                               NameValidator categoryNameValidator, EmailValidator orderEmailValidator, ServiceValidator<Order,
            Long> orderValidator, ServiceValidator<Tag, Long> tagValidator) {
        this.orderDAO = orderDAO;
        this.tagDAO = tagDAO;
        this.orderItemDAO = orderItemDAO;
        this.categoryNameValidator = categoryNameValidator;
        this.orderEmailValidator = orderEmailValidator;
        this.orderValidator = orderValidator;
        this.tagValidator = tagValidator;
    }

    @Override
    @Logging(startMessage = "Request on saving order to database is received.", endMessage = "Order is successfully saved to database.")
    public Order saveOrder(Order order) {
        order.fixAllNames();
        orderValidator.checkProperties(order);
        recreateOrder(order);
        order = orderDAO.create(order);
        return order;
    }

    @Override
    @Logging(startMessage = "Request on getting order by id from database is received.", endMessage = "Order is successfully get from database.")
    public Order getOrderById(Long id) {
        orderValidator.checkIdIsNotNull(id);
        return orderDAO.read(id);
    }

    @Override
    @Logging(startMessage = "Request on getting all orders from database is received.", endMessage = "Orders are successfully get from database.")
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    @Logging(startMessage = "Request on updating order (paying for it) in database is received.", endMessage = "Order is successfully updated in database.")
    public Order payForOrder(Long id) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderStatus() == OrderStatusEnum.CANCELED) {
            logger.error("Attempt to set not possible status.");
            throw new ServiceException("Sorry, you can't pay for canceled order. Create new one. With best regards");
        }
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);
        order = orderDAO.update(order);
        return order;
    }

    @Override
    @Logging(startMessage = "Request on updating order (setting next status) in database is received.", endMessage = "Order is successfully updated in database.")
    public Order nextStatus(Long id) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.NOT_PAID && order.getOrderStatus() == OrderStatusEnum.AGGREGATED) {
            logger.error("Attempt to set not possible status.");
            throw new ServiceException("Sorry, but we can't send order to you without payment. Please, pay firstly and then we will send you your order.");
        }
        if (order.getOrderStatus() == OrderStatusEnum.CANCELED) {
            logger.error("Attempt to set not possible status.");
            throw new ServiceException("Сорямба, we can't set next status of canceled order)))).");
        }
        order.setOrderStatus(order.getOrderStatus().nextStatus());
        order = orderDAO.update(order);
        return order;
    }

    @Override
    @Logging(startMessage = "Request on updating order (canceling order) in database is received.", endMessage = "Order is successfully updated in database.")
    public Order cancelOrder(Long id) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.PAID || (order.getOrderStatus() != OrderStatusEnum.IN_PROCESS && order.getOrderStatus() != OrderStatusEnum.AGGREGATED)) {
            logger.error("Attempt to set not possible status.");
            throw new ServiceException("We can't cancel shipped or paid order.");
        }
        order.setOrderStatus(OrderStatusEnum.CANCELED);
        order = orderDAO.update(order);
        return order;
    }

    @Override
    @Logging(startMessage = "Request on updating order (adding order item) in database is received.", endMessage = "Order is successfully updated in database.")
    public Order addOrderItem(Long id, OrderItem item) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.PAID || order.getOrderStatus() != OrderStatusEnum.IN_PROCESS
                && order.getOrderStatus() != OrderStatusEnum.AGGREGATED || order.getOrderStatus() == OrderStatusEnum.CANCELED) {
            logger.error("Attempt to modify unchangeable order.");
            throw new ServiceException("You can't update paid, canceled or shipped order.");
        }
        checkAndRecreateOrderItem(item);
        order.addItem(item);
        item.setOrder(order);
        order = orderDAO.update(order);
        order.postPersistAndUpdate();
        return order;
    }

    @Override
    @Logging(startMessage = "Request on updating order (deleting order item) in database is received.", endMessage = "Order is successfully updated in database.")
    public Order deleteOrderItem(Long id, Long itemId) {
        orderValidator.checkIdIsNotNull(id);
        Order order = orderDAO.read(id);
        orderValidator.checkFoundById(order);
        if (order.getOrderPaymentStatus() == OrderPaymentStatusEnum.PAID || order.getOrderStatus() != OrderStatusEnum.IN_PROCESS
                && order.getOrderStatus() != OrderStatusEnum.AGGREGATED || order.getOrderStatus() == OrderStatusEnum.CANCELED) {
            logger.error("Attempt to update unchangeable order");
            throw new ServiceException("You can't update paid, canceled or shipped order.");
        }
        OrderItem item = orderItemDAO.read(itemId);
        order.getOrderItems().remove(item);
        order = orderDAO.update(order);
        order.postPersistAndUpdate();

        return order;
    }

    @Override
    @Logging(startMessage = "Request on deleting order from database is received.", endMessage = "Order is successfully deleted from database.")
    public void deleteOrderById(Long id) {
        orderValidator.checkIdIsNotNull(id);
        try {
            orderDAO.delete(id);
        } catch (EntityNotFoundException exc) {
            logger.error("Attempt to delete not existent order.");
            throw new ServiceException("Order with id: \'" + id + "\' doesn't exist. You can't delete not existent order.");
        }
    }

    @Override
    @Logging(startMessage = "Request on getting all order items of customer by category from database is received.", endMessage = "Orders are successfully get from database.")
    public List<OrderItem> findCustomerOrderItemsByCategory(String customerEmail, String category) {
        categoryNameValidator.check(category);
        orderEmailValidator.check(customerEmail);
        return orderDAO.findCustomerOrderItemsByCategory(customerEmail, category);
    }

    @Override
    @Logging(startMessage = "Request on getting order items of customer by tag from database is received.", endMessage = "Orders are successfully get from database.")
    public List<OrderItem> findCustomerOrderItemsByTag(String customerEmail, Tag tag) {
        tag.fixName();
        tagValidator.checkProperties(tag);
        Tag foundedTag = tagDAO.readByName(tag.getTagname());
        tagValidator.checkFoundByName(foundedTag, tag.getTagname());
        orderEmailValidator.check(customerEmail);
        return orderDAO.findCustomerOrderItemsByTag(customerEmail, foundedTag);
    }

    @Override
    @Logging(startMessage = "Request on getting order items by payment status from database is received.", endMessage = "Orders are successfully get from database.")
    public List<Order> getAllOrdersByPaymentStatus(String paymentStatus) {
        OrderPaymentStatusEnum orderPaymentStatus = OrderPaymentStatusEnum.valueOf(paymentStatus.toUpperCase());
        return orderDAO.getAllOrdersByPaymentStatus(orderPaymentStatus);
    }

    @Override
    @Logging(startMessage = "Request on getting order by e-mail from database is received.", endMessage = "Orders are successfully get from database.")
    public List<Order> getAllOrdersByEmail(String email) {
        orderEmailValidator.check(email);
        return orderDAO.getAllOrdersByEmail(email);
    }

    @Override
    @Logging(startMessage = "Request on getting amount of items, bought by customer with e-mail, from database is received.", endMessage = "Amount is successfully get.")
    public Integer getAmountOfItemsBoughtByCustomerWithEmail(String email) {
        orderEmailValidator.check(email);
        return orderDAO.getAllOrdersByEmail(email).stream()
                .mapToInt(Order::getAmountOfOrderItems)
                .sum();
    }

    @Override
    @Logging(startMessage = "Request on getting full price of items, bought by customer with e-mail, from database is received.", endMessage = "Full price is successfully get.")
    public Double GetFullPriceOfItemsBoughtByCustomerWithEmail(String email) {
        orderEmailValidator.check(email);
        return orderDAO.getAllOrdersByEmail(email).stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    private void recreateOrder(Order order) {
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
