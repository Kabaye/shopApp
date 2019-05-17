package com.netcracker.edu.kulich.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    private Long id = 0L;
    private LocalDate date = LocalDate.now();
    private String email = "";
    private String orderStatus = "";
    private String orderPaymentStatus = "";
    private Set<OrderItemDTO> orderItems = new HashSet<>();
    private Double totalPrice = 0.0;
    private Integer amountOfItems = 0;

    public OrderDTO(LocalDate date, String email, Set<OrderItemDTO> orderItems) {
        this.date = date;
        this.email = email;
        this.orderItems = orderItems;
        this.orderStatus = "in_process";
        this.orderPaymentStatus = "not_paid";
    }

    public void fixOrderStatusAndPaymentStatus() {
        orderStatus = orderStatus.trim().replaceAll(" +", " ");
        orderPaymentStatus = orderPaymentStatus.trim().replaceAll(" +", " ");
    }
}
