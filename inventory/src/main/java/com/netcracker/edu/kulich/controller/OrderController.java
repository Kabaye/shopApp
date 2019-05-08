package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.OrderItemDTO;
import com.netcracker.edu.kulich.dto.PairIdNameDTO;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private Transformator transformator;

    @GetMapping(value = "/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders.stream()
                .map(transformator::convertToOrderDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/orders/{id:[\\d]+}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PostMapping(value = "/orders")
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.saveOrder(transformator.convertToOrderEntity(orderDTO));
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.CREATED);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/customer")
    public ResponseEntity<OrderDTO> updateOrderByCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("id") Long id) {
        Order order = orderService.updateCustomer(id, transformator.convertToCustomerEntity(customerDTO));
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/status")
    public ResponseEntity<OrderDTO> updateOrderByStatus(@RequestBody String status, @PathVariable("id") Long id) {
        Order order = orderService.updateStatus(id, status);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/payment-status")
    public ResponseEntity<OrderDTO> updateOrderByPaymentStatus(@RequestBody String paymentStatus, @PathVariable("id") Long id) {
        Order order = orderService.updatePaymentStatus(id, paymentStatus);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    public ResponseEntity<OrderDTO> updateOrderByOrderItems(@RequestBody OrderItemDTO item, @PathVariable("id") Long id) {

        Order order = orderService.addOrderItem(id, transformator.convertToOrderItemEntity(item));
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    public ResponseEntity<OrderDTO> deleteOrderItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        Order order = orderService.deleteOrderItem(id, itemId);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/customers/{id:[\\d]+}/tag")
    public ResponseEntity<List<OrderItemDTO>> getOrdersOfCustomerByTag(@RequestBody PairIdNameDTO tag, @PathVariable("id") Long id) {
        List<OrderItem> orderItems = orderService.findCustomerOrdersByTag(id, transformator.convertToTagEntity(tag));
        return new ResponseEntity<>(orderItems.stream().map(transformator::convertToOrderItemDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/customers/{id:[\\d]+}/category")
    public ResponseEntity<List<OrderItemDTO>> getOrdersOfCustomerByCategory(@RequestBody PairIdNameDTO category, @PathVariable("id") Long id) {
        List<OrderItem> orderItems = orderService.findCustomerOrdersByCategory(id, category.getName());
        return new ResponseEntity<>(orderItems.stream().map(transformator::convertToOrderItemDto).collect(Collectors.toList()), HttpStatus.OK);
    }
}
