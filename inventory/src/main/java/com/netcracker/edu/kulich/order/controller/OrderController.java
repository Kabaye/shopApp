package com.netcracker.edu.kulich.order.controller;

import com.netcracker.edu.kulich.exception.controller.ControllerException;
import com.netcracker.edu.kulich.logging.annotation.DefaultLogging;
import com.netcracker.edu.kulich.logging.annotation.Logging;
import com.netcracker.edu.kulich.order.dto.OrderDTO;
import com.netcracker.edu.kulich.order.dto.OrderItemDTO;
import com.netcracker.edu.kulich.order.dto.PairIdNameDTO;
import com.netcracker.edu.kulich.order.dto.transformator.Transformator;
import com.netcracker.edu.kulich.order.entity.Order;
import com.netcracker.edu.kulich.order.entity.OrderItem;
import com.netcracker.edu.kulich.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@DefaultLogging
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);
    private OrderService orderService;
    private Transformator transformator;

    @Autowired
    public OrderController(OrderService orderService, Transformator transformator) {
        this.orderService = orderService;
        this.transformator = transformator;
    }

    @GetMapping(value = "/orders")
    @Logging(startMessage = "Request for getting all orders by filter is received.", endMessage = "Response on request for getting all orders by filter is sent.", startFromNewLine = true)
    public ResponseEntity<List<OrderDTO>> getAllOrders(@RequestParam(required = false) String email,
                                                       @RequestParam(required = false) String status) {
        if (email != null) {
            email = fixSpaces(email);
            return new ResponseEntity<>(getAllOrdersByEmail(email), HttpStatus.OK);
        } else if (status != null) {
            status = fixSpaces(status);
            return new ResponseEntity<>(getAllOrdersByPaymentStatus(status), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(orderService.getAllOrders().stream()
                    .map(transformator::convertToOrderDto).collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/orders/{id:[\\d]+}")
    @Logging(startMessage = "Request for getting order by id is received.", endMessage = "Response on request for getting order by id is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            logger.error("Order is not found.");
            throw new ControllerException("Order with id: \'" + id + "\' doesn't exist.");
        }
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @PostMapping(value = "/orders")
    @Logging(startMessage = "Request for saving order is received.", endMessage = "Response on request for saving order is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) {
        Order order = orderService.saveOrder(transformator.convertToOrderEntity(orderDTO));
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.CREATED);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/next")
    @Logging(startMessage = "Request for setting next order status of order is received.",
            endMessage = "Response on request for setting next order status of order is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> setNextOrderStatus(@PathVariable("id") Long id) {
        return new ResponseEntity<>(transformator.convertToOrderDto(orderService.nextStatus(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/cancel")
    @Logging(startMessage = "Request for canceling order is received.", endMessage = "Response on request for canceling order is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable("id") Long id) {
        return new ResponseEntity<>(transformator.convertToOrderDto(orderService.cancelOrder(id)), HttpStatus.OK);
    }

    @PostMapping(value = "/orders/{id:[\\d]+}/pay")
    @Logging(startMessage = "Request for paying for order is received.", endMessage = "Response on request for paying for order is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> updateOrderByPaymentStatus(@PathVariable("id") Long id) {
        return new ResponseEntity<>(transformator.convertToOrderDto(orderService.payForOrder(id)), HttpStatus.OK);
    }

    @PutMapping(value = "/orders/{id:[\\d]+}/items")
    @Logging(startMessage = "Request for adding order item to order is received.",
            endMessage = "Response on request for adding order item to order is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> addItemToOrder(@RequestBody OrderItemDTO item, @PathVariable("id") Long id) {
        Order order = orderService.addOrderItem(id, transformator.convertToOrderItemEntity(item));
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}/items/{itemId:[\\d]+}")
    @Logging(startMessage = "Request for removing order item from order is received.",
            endMessage = "Response on request for removing order item from order is sent.", startFromNewLine = true)
    public ResponseEntity<OrderDTO> deleteItemFromOrder(@PathVariable("id") Long id, @PathVariable("itemId") Long itemId) {
        Order order = orderService.deleteOrderItem(id, itemId);
        return new ResponseEntity<>(transformator.convertToOrderDto(order), HttpStatus.OK);
    }

    @DeleteMapping(value = "/orders/{id:[\\d]+}")
    @Logging(startMessage = "Request for deleting order is received.", endMessage = "Response on request for deleting order is sent.", startFromNewLine = true)
    public ResponseEntity deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/customers/{email}/orders/items")
    @Logging(startMessage = "Request for getting all items by e-mail and filter is received.",
            endMessage = "Response on request for getting all orders by e-mail and filter is sent.", startFromNewLine = true)
    public ResponseEntity<List<OrderItemDTO>> getItemsOfCustomerByFilter(@PathVariable("email") String email,
                                                                         @RequestParam(required = false) String tag,
                                                                         @RequestParam(required = false) String category) {
        if (tag != null) {
            tag = fixSpaces(tag);
            return new ResponseEntity<>(getItemsOfCustomerByTag(new PairIdNameDTO(tag), email), HttpStatus.OK);
        } else if (category != null) {
            category = fixSpaces(category);
            return new ResponseEntity<>(getItemsOfCustomerByCategory(new PairIdNameDTO(category), email), HttpStatus.OK);
        } else {
            logger.error("No params.");
            throw new ControllerException("No parameters in request. At least one parameter is required.");
        }
    }

    @Logging(startMessage = "Request for getting all order items by e-mail and tag is received.",
            endMessage = "Response on request for getting all order items by e-mail and tag is sent.")
    public List<OrderItemDTO> getItemsOfCustomerByTag(PairIdNameDTO tag, String email) {
        List<OrderItem> orderItems = orderService.findCustomerOrderItemsByTag(email, transformator.convertToTagEntity(tag));
        return orderItems.stream().map(transformator::convertToOrderItemDto).collect(Collectors.toList());
    }

    @Logging(startMessage = "Request for getting all order items by e-mail and category is received.",
            endMessage = "Response on request for getting all order items by e-mail and category is sent.")
    public List<OrderItemDTO> getItemsOfCustomerByCategory(PairIdNameDTO category, String email) {
        List<OrderItem> orderItems = orderService.findCustomerOrderItemsByCategory(email, category.getName());
        return orderItems.stream().map(transformator::convertToOrderItemDto).collect(Collectors.toList());
    }

    @Logging(startMessage = "Request for getting all orders by payment status is received.",
            endMessage = "Response on request for getting all orders by payment status is sent.")
    public List<OrderDTO> getAllOrdersByPaymentStatus(String status) {
        List<Order> orders = orderService.getAllOrdersByPaymentStatus(status);
        return orders.stream().map(transformator::convertToOrderDto).collect(Collectors.toList());
    }

    @Logging(startMessage = "Request for getting all orders by e-mail is received.", endMessage = "Response on request for getting all orders by e-mail is sent.")
    public List<OrderDTO> getAllOrdersByEmail(String email) {
        List<Order> orders = orderService.getAllOrdersByEmail(email);
        return orders.stream().map(transformator::convertToOrderDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/orders/{email}/amount")
    @Logging(startMessage = "Request for getting amount of items, bought by customer with e-mail, is received.",
            endMessage = "Response on request for getting amount of items, bought by customer with e-mail, is sent.", startFromNewLine = true)
    public ResponseEntity<Integer> getAmountOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(orderService.getAmountOfItemsBoughtByCustomerWithEmail(email), HttpStatus.OK);
    }

    @GetMapping(value = "/orders/{email}/full-price")
    @Logging(startMessage = "Request for getting full price of items, bought by customer with e-mail, is received.",
            endMessage = "Response on request for getting full price of items, bought by customer with e-mail, is sent.", startFromNewLine = true)
    public ResponseEntity<Double> GetFullPriceOfItemsBoughtByCustomerWithEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(orderService.GetFullPriceOfItemsBoughtByCustomerWithEmail(email), HttpStatus.OK);
    }

    private String fixSpaces(String str) {
        return str.replaceAll("%20", " ");
    }
}
