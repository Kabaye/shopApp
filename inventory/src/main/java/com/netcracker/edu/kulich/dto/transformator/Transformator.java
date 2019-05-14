package com.netcracker.edu.kulich.dto.transformator;

import com.netcracker.edu.kulich.dto.CustomerDTO;
import com.netcracker.edu.kulich.dto.OrderDTO;
import com.netcracker.edu.kulich.dto.OrderItemDTO;
import com.netcracker.edu.kulich.dto.PairIdNameDTO;
import com.netcracker.edu.kulich.entity.*;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Transformator {
    public Order convertToOrderEntity(OrderDTO orderDTO) {
        Order order = new Order();

        Customer customer = new Customer();
        customer.setAge(orderDTO.getCustomer().getAge());
        customer.setFio(orderDTO.getCustomer().getFio());
        order.setCustomer(customer);

        order.setDate(orderDTO.getDate());

        for (OrderItemDTO orderItem : orderDTO.getOrderItems()) {
            OrderItem item = convertToOrderItemEntity(orderItem);
            item.setOrder(order);
            order.addOffer(item);
        }

        orderDTO.fixOrderStatusAndPaymentStatus();

        order.setOrderPaymentStatus(OrderPaymentStatusEnum.valueOf(orderDTO.getOrderPaymentStatus().toUpperCase()));
        order.setOrderStatus(OrderStatusEnum.valueOf(orderDTO.getOrderStatus().toUpperCase()));

        return order;
    }

    public OrderDTO convertToOrderDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAge(order.getCustomer().getAge());
        customerDTO.setFio(order.getCustomer().getFio());
        customerDTO.setId(order.getCustomer().getId());
        orderDTO.setCustomer(customerDTO);

        orderDTO.setAmountOfItems(order.getAmountOfOrderItems());

        orderDTO.setDate(order.getDate());

        orderDTO.setId(order.getId());

        orderDTO.setOrderPaymentStatus(order.getOrderPaymentStatus().toString().toLowerCase());
        orderDTO.setOrderStatus(order.getOrderStatus().toString().toLowerCase());

        orderDTO.setTotalPrice(order.getTotalPrice());

        orderDTO.setOrderItems(order.getOrderItems().stream()
                .map(this::convertToOrderItemDto)
                .collect(Collectors.toSet())
        );

        return orderDTO;
    }

    public Tag convertToTagEntity(PairIdNameDTO tagDTO) {
        Tag tag = new Tag();
        tag.setTagname(tagDTO.getName());
        return tag;
    }

    public PairIdNameDTO convertToTagDto(Tag tag) {
        return new PairIdNameDTO(tag.getId(), tag.getTagname());
    }

    public OrderItem convertToOrderItemEntity(OrderItemDTO orderItem) {
        OrderItem item = new OrderItem();
        item.setCategory(orderItem.getCategory());
        item.setName(orderItem.getName());
        item.setPrice(orderItem.getPrice());
        item.setTags(orderItem.getTags().stream()
                .filter(Objects::nonNull)
                .map(this::convertToTagEntity)
                .collect(Collectors.toSet()));
        return item;
    }

    public OrderItemDTO convertToOrderItemDto(OrderItem orderItem) {
        OrderItemDTO item = new OrderItemDTO();
        item.setCategory(orderItem.getCategory());
        item.setId(orderItem.getId());
        item.setName(orderItem.getName());
        item.setOrderId(orderItem.getOrder().getId());
        item.setPrice(orderItem.getPrice());
        item.setTags(orderItem.getTags().stream()
                .map(this::convertToTagDto)
                .collect(Collectors.toSet())
        );
        return item;
    }

    public Customer convertToCustomerEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setAge(customerDTO.getAge());
        customer.setFio(customerDTO.getFio());
        return customer;
    }

    public CustomerDTO convertToCustomerDto(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setAge(customer.getAge());
        customerDTO.setFio(customer.getFio());
        customerDTO.setId(customer.getId());
        return customerDTO;
    }
}
