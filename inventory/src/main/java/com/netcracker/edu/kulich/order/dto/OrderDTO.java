package com.netcracker.edu.kulich.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class OrderDTO {
    private Long id = 0L;
    private LocalDate date = LocalDate.now();
    private String email = "";
    private String orderStatus = "";
    private String orderPaymentStatus = "";
    private Set<OrderItemDTO> orderItems = new HashSet<>();
    private Double totalPrice = 0.0;
    private Integer amountOfItems = 0;

    public void fixOrderStatusAndPaymentStatus() {
        orderStatus = orderStatus.trim().replaceAll(" +", " ");
        orderPaymentStatus = orderPaymentStatus.trim().replaceAll(" +", " ");
    }
}
