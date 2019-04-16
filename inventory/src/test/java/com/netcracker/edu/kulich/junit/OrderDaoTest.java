package com.netcracker.edu.kulich.junit;


import com.netcracker.edu.kulich.dao.OrderDAO;
import com.netcracker.edu.kulich.dao.OrderDAOManager;
import com.netcracker.edu.kulich.entity.*;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OrderDaoTest {
    OrderDAO orderDAO = new OrderDAOManager();

    @Test
    public void createOrderTest() {
        PropertyConfigurator.configure("D:\\Univer\\NetCracker\\MainProject\\inventory\\properties\\log4j.properties");
        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");
        offer.addTag(tag);

        Offer offer1 = new Offer();
        offer1.setName("of2");

        price = new Price();
        price.setPrice(2500d);

        offer1.setCategory(category);
        offer1.setPrice(price);

        tag = new Tag();
        tag.setTagname("tag3");
        offer1.addTag(tag);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(offer);
        order.addOffer(offer1);
        order.setDate(date);

        order.setCustomer(customer);

        order.setOrderStatus(OrderStatusEnum.AGGREGATED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        order = orderDAO.create(order);

        offer = (Offer) order.getOrderItems().toArray()[0];
        offer1 = (Offer) order.getOrderItems().toArray()[1];
        customer = order.getCustomer();

        Order order1 = new Order();

        order1.addOffer(offer);
        order1.addOffer(offer1);
        order1.setDate(date);
        order1.setCustomer(customer);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        order1 = orderDAO.create(order1);

        Order order2 = orderDAO.read(order.getId());
        Order order3 = orderDAO.read(order1.getId());

        assertEquals(order, order2);
        assertEquals(order1, order3);
    }

    @Test
    public void findAllOrdersTest() {
        List<Order> orderList = orderDAO.findAll();
        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");
        offer.addTag(tag);

        Offer offer1 = new Offer();
        offer1.setName("of2");

        price = new Price();
        price.setPrice(2500d);

        offer1.setCategory(category);
        offer1.setPrice(price);

        tag = new Tag();
        tag.setTagname("tag3");
        offer1.addTag(tag);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(offer);
        order.addOffer(offer1);
        order.setDate(date);

        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        order.setCustomer(customer);

        order = orderDAO.create(order);

        orderList.add(order);

        offer = (Offer) order.getOrderItems().toArray()[0];
        offer1 = (Offer) order.getOrderItems().toArray()[1];
        customer = order.getCustomer();

        Order order1 = new Order();

        order1.addOffer(offer);
        order1.addOffer(offer1);
        order1.setDate(date);
        order1.setCustomer(customer);
        order1.setOrderStatus(OrderStatusEnum.DELIVERED);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

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
        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");
        offer.addTag(tag);

        Offer offer1 = new Offer();
        offer1.setName("of2");

        price = new Price();
        price.setPrice(2500d);

        offer1.setCategory(category);
        offer1.setPrice(price);

        tag = offer.getTags().iterator().next();

        offer1.addTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname("tag3");
        offer1.addTag(tag1);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(offer);
        order.addOffer(offer1);
        order.setDate(date);

        order.setCustomer(customer);
        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        order = orderDAO.create(order);

        offer = (Offer) order.getOrderItems().toArray()[0];
        offer1 = (Offer) order.getOrderItems().toArray()[1];

        order.getOrderItems().remove(offer);

        order.setDate(date);
        customer = new Customer();
        customer.setFio("new FIO1");
        customer.setAge(100);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatusEnum.AGGREGATED);

        Order order1 = orderDAO.update(order);


        assertEquals(order, order1);
    }

    @Test
    public void deleteOrderTest() {
        Offer offer = new Offer();
        offer.setName("of1");

        Category category = new Category();
        category.setCategory("cat1");

        Price price = new Price();
        price.setPrice(1950d);

        offer.setCategory(category);
        offer.setPrice(price);

        Tag tag = new Tag();
        tag.setTagname("tag1");
        offer.addTag(tag);

        tag = new Tag();
        tag.setTagname("tag2");
        offer.addTag(tag);

        Offer offer1 = new Offer();
        offer1.setName("of2");

        price = new Price();
        price.setPrice(2500d);

        offer1.setCategory(category);
        offer1.setPrice(price);

        tag = offer.getTags().iterator().next();

        offer1.addTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname("tag3");
        offer1.addTag(tag1);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("FIO1");
        customer.setAge(100);

        Order order = new Order();
        order.addOffer(offer);
        order.addOffer(offer1);
        order.setDate(date);

        order.setCustomer(customer);

        order.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        order = orderDAO.create(order);

        orderDAO.delete(order.getId());

        assertNull(orderDAO.read(order.getId()));
    }

}
