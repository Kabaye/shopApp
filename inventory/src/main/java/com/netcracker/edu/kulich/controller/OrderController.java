package com.netcracker.edu.kulich.controller;

import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.OrderItemDTO;
import com.netcracker.edu.kulich.dto.PairIdNameDTO;
import com.netcracker.edu.kulich.dto.transformator.Transformator;
import com.netcracker.edu.kulich.entity.Order;
import com.netcracker.edu.kulich.entity.OrderItem;
import com.netcracker.edu.kulich.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class OrderController {

    private OrderService orderService;
    private Transformator transformator;

    public OrderController(OrderService orderService, Transformator transformator) {
        this.orderService = orderService;
        this.transformator = transformator;
    }

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

    @PutMapping(value = "/orders/{id:[\\d]+}/status/next")
    public ResponseEntity<OrderDTO> updateOrderByStatus(@PathVariable("id") Long id) {
        Order order = orderService.nextStatus(id);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/pay")
    public ResponseEntity<OrderDTO> updateOrderByPaymentStatus(@PathVariable("id") Long id) {
        Order order = orderService.payForOrder(id);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    public ResponseEntity<OrderDTO> addItemToOrder(@RequestBody OrderItemDTO item, @PathVariable("id") Long id) {
        Order order = orderService.addOrderItem(id, transformator.convertToOrderItemEntity(item));
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    public ResponseEntity<OrderDTO> deleteItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        Order order = orderService.deleteOrderItem(id, itemId);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}")
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/customers/{email}/tag")
    public ResponseEntity<List<OrderItemDTO>> getOrdersOfCustomerByTag(@RequestBody PairIdNameDTO tag, @PathVariable("email") String email) {
        List<OrderItem> orderItems = orderService.findCustomerOrderItemsByTag(email, transformator.convertToTagEntity(tag));
        return new ResponseEntity<>(orderItems.stream().map(transformator::convertToOrderItemDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/customers/{email}/category")
    public ResponseEntity<List<OrderItemDTO>> getOrdersOfCustomerByCategory(@RequestBody PairIdNameDTO category, @PathVariable("email") String email) {
        List<OrderItem> orderItems = orderService.findCustomerOrderItemsByCategory(email, category.getName());
        return new ResponseEntity<>(orderItems.stream().map(transformator::convertToOrderItemDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/statuses/{status}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByPaymentStatus(@PathVariable("status") String status) {
        List<Order> orders = orderService.getAllOrdersByPaymentStatus(status);
        return new ResponseEntity<>(orders.stream().map(transformator::convertToOrderDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/emails/{email}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByEmail(@PathVariable("email") String email) {
        List<Order> orders = orderService.getAllOrdersByEmail(email);
        return new ResponseEntity<>(orders.stream().map(transformator::convertToOrderDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(value = "/emails/{email}/amount")
    public ResponseEntity<Integer> getAmountOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(orderService.getAmountOfItemsBoughtByCustomerWithEmail(email), HttpStatus.OK);
    }

    @GetMapping(value = "/emails/{email}/full-price")
    public ResponseEntity<Double> GetFullPriceOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(orderService.GetFullPriceOfItemsBoughtByCustomerWithEmail(email), HttpStatus.OK);
    }
}
