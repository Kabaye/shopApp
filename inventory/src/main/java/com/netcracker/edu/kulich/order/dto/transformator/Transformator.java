package com.netcracker.edu.kulich.order.dto.transformator;

import com.netcracker.edu.kulich.order.dto.OrderDTO;
import com.netcracker.edu.kulich.order.dto.OrderItemDTO;
import com.netcracker.edu.kulich.order.dto.PairIdNameDTO;
import com.netcracker.edu.kulich.order.entity.*;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class Transformator {
    public Order convertToOrderEntity(OrderDTO orderDTO) {
        Order order = new Order();

        order.setEmail(orderDTO.getEmail());

        order.setDate(orderDTO.getDate());

        for (OrderItemDTO orderItem : orderDTO.getOrderItems()) {
            OrderItem item = convertToOrderItemEntity(orderItem);
            item.setOrder(order);
            order.addItem(item);
        }

        orderDTO.fixOrderStatusAndPaymentStatus();

        order.setOrderPaymentStatus(OrderPaymentStatusEnum.NOT_PAID);
        order.setOrderStatus(OrderStatusEnum.IN_PROCESS);

        return order;
    }

    public OrderDTO convertToOrderDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setEmail(order.getEmail());

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
}
