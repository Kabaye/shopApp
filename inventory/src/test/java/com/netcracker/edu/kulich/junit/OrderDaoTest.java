package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.dao.OrderDAO;
import com.netcracker.edu.kulich.dao.OrderDAOManager;
import com.netcracker.edu.kulich.entity.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrderDaoTest {
    private OrderDAO orderDAO = new OrderDAOManager();

    @Test
    public void createOrderTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setName("createOrder_offer1");

        Category category = new Category();
        category.setCategory("createOrder_category1");

        Price price = new Price();
        price.setPrice(1950d);

        orderItem.setCategory(category);
        orderItem.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("createOrder_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("createOrder_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("createOrder_offer2");

        price = new Price();
        price.setPrice(2500d);

        orderItem1.setCategory(category);
        orderItem1.setPrice(price);

        tag = new Tag();
        tag.setTagname("createOrder_tag3");
        orderItem1.addTag(tag);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("createOrder_FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(orderItem);
        order.addOffer(orderItem1);
        order.setDate(date);

        order.setCustomer(customer);

        order.setOrderStatus(OrderStatusEnum.AGGREGATED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        orderItem.setOrder(order);

        orderItem1.setOrder(order);

        order = orderDAO.create(order);

        category = ((OrderItem) order.getOrderItems().toArray()[0]).getCategory();

        customer = order.getCustomer();

        Order order1 = new Order();

        orderItem = new OrderItem();

        orderItem.setName("createOrder_offer3");

        tag = new Tag();
        tag.setTagname("createOrder_tag4");
        orderItem.addTag(tag);

        orderItem.setPrice(price);

        orderItem.setCategory(category);

        order1.addOffer(orderItem);

        order1.setDate(date);
        order1.setCustomer(customer);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem.setOrder(order1);

        order1 = orderDAO.create(order1);

        Order order2 = orderDAO.read(order.getId());
        Order order3 = orderDAO.read(order1.getId());

        assertEquals(order, order2);
        assertEquals(order1, order3);
    }

    @Test
    public void findAllOrdersTest() {
        List<Order> orderList = orderDAO.findAll();

        OrderItem orderItem = new OrderItem();

        orderItem.setName("findAllOrders_offer1");

        Category category = new Category();
        category.setCategory("findAllOrders_cat1");

        Price price = new Price();
        price.setPrice(1950d);

        orderItem.setCategory(category);
        orderItem.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("findAllOrders_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("findAllOrders_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("findAllOrders_offer2");

        price = new Price();
        price.setPrice(2500d);

        orderItem1.setCategory(category);
        orderItem1.setPrice(price);

        tag = new Tag();
        tag.setTagname("findAllOrders_tag3");
        orderItem1.addTag(tag);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("findAllOrders_FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(orderItem);
        order.addOffer(orderItem1);
        order.setDate(date);

        orderItem.setOrder(order);
        orderItem1.setOrder(order);

        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        order.setCustomer(customer);

        order = orderDAO.create(order);

        orderList.add(order);

        category = ((OrderItem) order.getOrderItems().toArray()[0]).getCategory();

        customer = order.getCustomer();

        Order order1 = new Order();

        orderItem = new OrderItem();

        orderItem.setName("findAllOrders_offer3");

        tag = new Tag();
        tag.setTagname("findAllOrders_tag4");
        orderItem.addTag(tag);

        orderItem.setPrice(price);

        orderItem.setCategory(category);

        order1.addOffer(orderItem);

        order1.setDate(date);
        order1.setCustomer(customer);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem.setOrder(order1);

        order1 = orderDAO.create(order1);

        orderList.add(order1);

        List<Order> orders = orderDAO.findAll();

        assertEquals(orderList.size(), orders.size());
        for (int i = 0; i < orderList.size(); i++) {
            assertEquals(orderList.get(i), orders.get(i));
        }
    }

    @Test
    public void updateOrderTest() {
        OrderItem orderItem = new OrderItem();

        orderItem.setName("updateOrder_offer1");

        Category category = new Category();
        category.setCategory("updateOrder_cat1");

        Price price = new Price();
        price.setPrice(1950d);

        orderItem.setCategory(category);
        orderItem.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("updateOrder_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("updateOrder_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("updateOrder_offer2");

        price = new Price();
        price.setPrice(2500d);

        orderItem1.setCategory(category);
        orderItem1.setPrice(price);

        tag = new Tag();
        tag.setTagname("updateOrder_tag3");
        orderItem1.addTag(tag);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("updateOrder_FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(orderItem);
        order.addOffer(orderItem1);
        order.setDate(date);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        orderItem.setOrder(order);
        orderItem1.setOrder(order);

        order = orderDAO.create(order);

        orderItem = (OrderItem) order.getOrderItems().toArray()[0];

        order.getOrderItems().remove(orderItem);

        customer = new Customer();
        customer.setFio("new updateOrder_FIO1");
        customer.setAge(55);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatusEnum.AGGREGATED);

        Order order1 = orderDAO.update(order);

        order.setCustomer(order1.getCustomer());
        order.setAmountOfOrderItems(order.getOrderItems().size());
        order.setTotalPrice(0D);
        for (OrderItem item : order.getOrderItems()) {
            order.setTotalPrice(order.getTotalPrice() + item.getPrice().getPrice());
        }
        assertEquals(order, order1);
    }

    @Test
    public void deleteOrderTest() {
        OrderItem orderItem = new OrderItem();

        orderItem.setName("deleteOrder_offer1");

        Category category = new Category();
        category.setCategory("deleteOrder_cat1");

        Price price = new Price();
        price.setPrice(1950d);

        orderItem.setCategory(category);
        orderItem.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("deleteOrder_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("deleteOrder_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("deleteOrder_offer2");

        price = new Price();
        price.setPrice(2500d);

        orderItem1.setCategory(category);
        orderItem1.setPrice(price);

        tag = new Tag();
        tag.setTagname("deleteOrder_tag3");
        orderItem1.addTag(tag);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("deleteOrder_FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(orderItem);
        order.addOffer(orderItem1);
        order.setDate(date);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        orderItem.setOrder(order);
        orderItem1.setOrder(order);

        order = orderDAO.create(order);

        orderDAO.delete(order.getId());

        assertNull(orderDAO.read(order.getId()));
    }
}
