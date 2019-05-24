package com.netcracker.edu.kulich.order.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum OrderStatusEnum {
    IN_PROCESS {
        public OrderStatusEnum nextStatus() {
            return AGGREGATED;
        }
    }, // в процессе сборки
    AGGREGATED {
        public OrderStatusEnum nextStatus() {
            return SHIPPED;
        }
    }, // собран
    SHIPPED {
        public OrderStatusEnum nextStatus() {
            return DELIVERED;
        }
    }, // отправлен
    DELIVERED {
        public OrderStatusEnum nextStatus() {
            return DELIVERED;
        }
    }, // доставлен
    CANCELED {
        public OrderStatusEnum nextStatus() {
            return CANCELED;
        }
    }; // отменен

    public abstract OrderStatusEnum nextStatus();
}
