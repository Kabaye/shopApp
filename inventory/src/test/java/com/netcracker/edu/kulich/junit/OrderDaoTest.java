package com.netcracker.edu.kulich.junit;

import com.netcracker.edu.kulich.entity.*;
import com.netcracker.edu.kulich.exception.service.OrderServiceException;
import com.netcracker.edu.kulich.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Deprecated
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderDaoTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void createOrderTest() throws OrderServiceException {
        OrderItem orderItem = new OrderItem();
        orderItem.setName("createOrder_offer1");

        orderItem.setCategory("createOrder_category1");
        orderItem.setPrice(123.50);

        Tag tag = new Tag();
        tag.setTagname("createOrder_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("createOrder_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("createOrder_offer2");

        orderItem1.setCategory("createOrder_category1");
        orderItem1.setPrice(1653.99);

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

        order = orderService.saveOrder(order);

        customer = order.getCustomer();

        Order order1 = new Order();

        orderItem = new OrderItem();

        orderItem.setName("createOrder_offer3");

        tag = new Tag();
        tag.setTagname("createOrder_tag4");
        orderItem.addTag(tag);

        orderItem.setPrice(5688.50);

        orderItem.setCategory("createOrder_category1");

        order1.addOffer(orderItem);

        order1.setDate(date);
        order1.setCustomer(customer);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem.setOrder(order1);

        order1 = orderService.saveOrder(order1);

        Order order2 = orderService.getOrderById(order.getId());
        Order order3 = orderService.getOrderById(order1.getId());

        assertEquals(order, order2);
        assertEquals(order1, order3);
    }

    @Test
    public void findAllOrdersTest() throws OrderServiceException {
        List<Order> orderList = orderService.getAllOrders();

        OrderItem orderItem = new OrderItem();

        orderItem.setName("findAllOrders_offer1");

        orderItem.setCategory("findAllOrders_cat1");
        orderItem.setPrice(4359.99);

        Tag tag = new Tag();
        tag.setTagname("findAllOrders_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("findAllOrders_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("findAllOrders_offer2");

        orderItem1.setCategory("findAllOrders_cat2");
        orderItem1.setPrice(4199.99);

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

        order = orderService.saveOrder(order);

        orderList.add(order);

        customer = order.getCustomer();

        Order order1 = new Order();

        orderItem = new OrderItem();

        orderItem.setName("findAllOrders_offer3");

        tag = new Tag();
        tag.setTagname("findAllOrders_tag4");
        orderItem.addTag(tag);

        orderItem.setPrice(1255.99);

        orderItem.setCategory("findAllOrders_cat3");

        order1.addOffer(orderItem);

        order1.setDate(date);
        order1.setCustomer(customer);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem.setOrder(order1);

        order1 = orderService.saveOrder(order1);

        orderList.add(order1);

        List<Order> orders = orderService.getAllOrders();

        assertEquals(orderList.size(), orders.size());
        for (int i = 0; i < orderList.size(); i++) {
            assertEquals(orderList.get(i), orders.get(i));
        }
    }

    @Test
    public void updateOrderTest() throws OrderServiceException {
        OrderItem orderItem = new OrderItem();

        orderItem.setName("updateOrder_offer1");

        orderItem.setCategory("updateOrder_cat1");
        orderItem.setPrice(1466.99);

        Tag tag = new Tag();
        tag.setTagname("updateOrder_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("updateOrder_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("updateOrder_offer2");

        orderItem1.setCategory("updateOrder_cat1");
        orderItem1.setPrice(1433.99);

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

        order = orderService.saveOrder(order);

        orderItem = (OrderItem) order.getOrderItems().toArray()[0];

        order.getOrderItems().remove(orderItem);

        customer = new Customer();
        customer.setFio("new updateOrder_FIO1");
        customer.setAge(55);
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatusEnum.AGGREGATED);

        Order order1 = orderService.updateCustomer(order.getId(), order.getCustomer());

        order.setCustomer(order1.getCustomer());
        order.setAmountOfOrderItems(order.getOrderItems().size());
        order.setTotalPrice(0D);
        for (OrderItem item : order.getOrderItems()) {
            order.setTotalPrice(order.getTotalPrice() + item.getPrice());
        }
        assertEquals(order, order1);
    }

    @Test
    public void deleteOrderTest() throws OrderServiceException {
        OrderItem orderItem = new OrderItem();

        orderItem.setName("deleteOrder_offer1");

        orderItem.setCategory("deleteOrder_cat1");
        orderItem.setPrice(4999.99);

        Tag tag = new Tag();
        tag.setTagname("deleteOrder_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("deleteOrder_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("deleteOrder_offer2");

        orderItem1.setCategory("deleteOrder_cat1");
        orderItem1.setPrice(2499.99);

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

        order = orderService.saveOrder(order);

        orderService.deleteOrderById(order.getId());

        assertNull(orderService.getOrderById(order.getId()));
    }

    @Test
    public void findAllOrderItemsByCustomerAndCategoryTest() throws OrderServiceException {
        OrderItem orderItem = new OrderItem();

        orderItem.setName("findAllOrderItemsByCustomerAndCategory_offer1");

        orderItem.setCategory("findAllOrderItemsByCustomerAndCategory_cat1");
        orderItem.setPrice(1599.99);

        Tag tag = new Tag();
        tag.setTagname("findAllOrderItemsByCustomerAndCategory_tag1");
        orderItem.addTag(tag);

        tag = new Tag();
        tag.setTagname("findAllOrderItemsByCustomerAndCategory_tag2");
        orderItem.addTag(tag);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("findAllOrderItemsByCustomerAndCategory_offer2");

        orderItem1.setCategory("findAllOrderItemsByCustomerAndCategory_cat1");
        orderItem1.setPrice(1799.99);

        tag = new Tag();
        tag.setTagname("findAllOrderItemsByCustomerAndCategory_tag3");
        orderItem1.addTag(tag);

        Customer customer = new Customer();
        customer.setFio("findAllOrderItemsByCustomerAndCategory_FIO1");
        customer.setAge(100);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Order order = new Order();
        order.addOffer(orderItem);
        order.addOffer(orderItem1);
        order.setDate(date);

        orderItem.setOrder(order);
        orderItem1.setOrder(order);

        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        order.setCustomer(customer);

        order = orderService.saveOrder(order);

        orderItem = (OrderItem) order.getOrderItems().toArray()[0];
        customer = order.getCustomer();

        Customer customer1 = new Customer();
        customer1.setFio("findAllOrderItemsByCustomerAndCategory_FIO2");
        customer1.setAge(55);

        Order order1 = new Order();

        OrderItem orderItem2 = new OrderItem();

        orderItem2.setName("findAllOrderItemsByCustomerAndCategory_offer3");

        tag = new Tag();
        tag.setTagname("findAllOrderItemsByCustomerAndCategory_tag4");
        orderItem2.addTag(tag);

        orderItem2.setPrice(1566.99);

        orderItem2.setCategory("findAllOrderItemsByCustomerAndCategory_cat1");

        order1.addOffer(orderItem2);

        order1.setDate(date);
        order1.setCustomer(customer1);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem2.setOrder(order1);

        order1 = orderService.saveOrder(order1);
        customer1 = order1.getCustomer();

        assertEquals(1, orderService.findCustomerOrdersByCategory(customer1.getId(), "findAllOrderItemsByCustomerAndCategory_cat1").size());
        assertEquals(2, orderService.findCustomerOrdersByCategory(customer.getId(), "findAllOrderItemsByCustomerAndCategory_cat1").size());
    }

    @Test
    public void findAllOrderItemsByCustomerAndTagTest() throws OrderServiceException {
        OrderItem orderItem = new OrderItem();

        orderItem.setName("findAllOrderItemsByCustomerAndTag_offer1");

        orderItem.setCategory("findAllOrderItemsByCustomerAndTag_cat1");
        orderItem.setPrice(2799.99);

        Tag tag = new Tag();
        tag.setTagname("findAllOrderItemsByCustomerAndTag_tag1");
        orderItem.addTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname("findAllOrderItemsByCustomerAndTag_tag2");
        orderItem.addTag(tag1);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("findAllOrderItemsByCustomerAndTag_offer2");

        orderItem1.setCategory("findAllOrderItemsByCustomerAndTag_cat1");
        orderItem1.setPrice(1799.99);

        Tag tag2 = new Tag();
        tag2.setTagname("findAllOrderItemsByCustomerAndTag_tag3");
        orderItem1.addTag(tag2);

        Customer customer = new Customer();
        customer.setFio("findAllOrderItemsByCustomerAndTag_FIO1");
        customer.setAge(100);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Order order = new Order();
        order.addOffer(orderItem);
        order.addOffer(orderItem1);
        order.setDate(date);

        orderItem.setOrder(order);
        orderItem1.setOrder(order);

        order.setOrderStatus(OrderStatusEnum.SHIPPED);
        order.setOrderPaymentStatus(OrderPaymentStatusEnum.PAID);

        order.setCustomer(customer);

        order = orderService.saveOrder(order);

        orderItem = (OrderItem) order.getOrderItems().toArray()[0];
        System.out.println(orderItem.getTags().size());
        orderItem1 = (OrderItem) order.getOrderItems().toArray()[1];
        System.out.println(orderItem1.getTags().size());
        Iterator<Tag> it = orderItem.getTags().iterator();
        Iterator<Tag> it1 = orderItem1.getTags().iterator();
        tag = it.next();
        if (orderItem.getTags().size() == 1) {
            tag1 = it1.next();
        } else {
            tag1 = it.next();
        }
        tag2 = it1.next();

        orderItem1.addTag(tag);
        orderService.updateCustomer(order.getId(), order.getCustomer());

        customer = order.getCustomer();

        Customer customer1 = new Customer();
        customer1.setFio("findAllOrderItemsByCustomerAndTag_FIO2");
        customer1.setAge(55);

        Order order1 = new Order();

        OrderItem orderItem2 = new OrderItem();

        orderItem2.setName("findAllOrderItemsByCustomerAndTag_offer3");

        Tag tag3 = new Tag();
        tag3.setTagname("findAllOrderItemsByCustomerAndTag_tag4");
        orderItem2.addTag(tag3);

        orderItem2.setPrice(1488.0);

        orderItem2.setCategory("findAllOrderItemsByCustomerAndTag_cat1");

        order1.addOffer(orderItem2);

        order1.setDate(date);
        order1.setCustomer(customer1);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem2.setOrder(order1);

        order1 = orderService.saveOrder(order1);
        customer1 = order1.getCustomer();

        tag3 = order1.getOrderItems().iterator().next().getTags().iterator().next();

        assertEquals(2, orderService.findCustomerOrdersByTag(customer.getId(), tag).size());
        assertEquals(0, orderService.findCustomerOrdersByTag(customer1.getId(), tag).size());
        assertEquals(1, orderService.findCustomerOrdersByTag(customer.getId(), tag1).size());
        assertEquals(0, orderService.findCustomerOrdersByTag(customer1.getId(), tag2).size());
        assertEquals(1, orderService.findCustomerOrdersByTag(customer1.getId(), tag3).size());
    }

    /*@Test
    public void checkExceptionIfDataIsInvalidTest() throws OrderServiceException {
        OrderItem orderItem = new OrderItem();
        orderItem.setName("createOrder_offer1");

        orderItem.setCategory("createOrder_category1");
        orderItem.setPrice(123.50);

        Tag tag = new Tag();
        tag.setTagname("checkExceptionIfDataIsInvalid_tag1");
        orderItem.addTag(tag);

        Tag tag1 = new Tag();
        tag1.setTagname("checkExceptionIfDataIsInvalid_tag2");
        orderItem.addTag(tag1);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("checkExceptionIfDataIsInvalid_offer2");

        orderItem1.setCategory("checkExceptionIfDataIsInvalid_category1");
        orderItem1.setPrice(1653.99);

        Tag tag2 = new Tag();
        tag2.setTagname("checkExceptionIfDataIsInvalid_tag3");
        orderItem1.addTag(tag2);

        LocalDate date = LocalDate.of(2019, 04, 15);

        Customer customer = new Customer();
        customer.setFio("checkExceptionIfDataIsInvalid_FIO1");
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

        order = orderService.saveOrder(order);

        customer = order.getCustomer();

        Order order1 = new Order();

        orderItem = new OrderItem();

        orderItem.setName("checkExceptionIfDataIsInvalid_offer3");

        orderItem.addTag(tag);
        orderItem.addTag(tag1);
        orderItem.addTag(tag2);

        orderItem.setPrice(5688.50);

        orderItem.setCategory("checkExceptionIfDataIsInvalid_category1");

        order1.addOffer(orderItem);

        order1.setDate(date);
        order1.setCustomer(customer);

        order1.setOrderStatus(OrderStatusEnum.IN_PROCESS);
        order1.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);

        orderItem.setOrder(order1);

        order1 = orderService.saveOrder(order1);

        order1.setDate(LocalDate.of(1999, 1, 10));

        try {
            orderService.updateCustomer(order1);
        } catch (OrderServiceException exc) {
            assertTrue(true);
        }

        order1.setOrderStatus(null);
        order1.setOrderPaymentStatus(null);

        try {
            orderService.updateCustomer(order1);
        } catch (OrderServiceException exc) {
            assertTrue(true);
        }

        customer = new Customer();
        customer.setAge(155);
        customer.setFio("fio");
        order1.setCustomer(customer);

        try {
            orderService.updateCustomer(order1);
        } catch (OrderServiceException exc) {
            assertTrue(true);
        }

        customer.setAge(100);
        customer.setFio("");
        order1.setCustomer(customer);

        try {
            orderService.updateCustomer(order1);
        } catch (OrderServiceException exc) {
            assertTrue(true);
        }

        order1.getOrderItems().clear();

        try {
            orderService.updateCustomer(order1);
        } catch (OrderServiceException exc) {
            assertTrue(true);
        }
    }*/
}
