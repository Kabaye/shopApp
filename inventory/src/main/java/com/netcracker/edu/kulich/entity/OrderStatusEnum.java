package com.netcracker.edu.kulich.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum OrderStatusEnum {
    IN_PROCESS, //в процессе сборки
    AGGREGATED, //собран
    SHIPPED, //отправлен
    DELIVERED, // доставлен
    CANCELED
}
